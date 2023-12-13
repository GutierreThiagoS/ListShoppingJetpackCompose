package com.gutierre.mylists.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gutierre.mylists.data.local.dao.CategoryDao
import com.gutierre.mylists.data.local.dao.ProductDao
import com.gutierre.mylists.data.local.dao.ProductFireBaseDao
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductFireBase
import com.gutierre.mylists.framework.utils.dateFormat
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.state.StateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class FireStoreRepositoryImp(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val fireBaseDao: ProductFireBaseDao
): FireStoreRepository {

    companion object {
        const val CATEGORY_FIREBASE = "Compartilhado"
    }

    override fun getFireBaseAllFlow(): Flow<List<ProductFireBase>> {
        return fireBaseDao.getAllFlow()
    }

    override fun getProductFireBaseInRoom(description: String): ProductFireBase? {
        return fireBaseDao.getProductInFireBase(description)
    }

    override fun saveProductFirebaseStore(
        product: Product,
        result: (StateInfo) -> Unit
    ) {

        logE("SAVE FirebaseStore saveProductFirebaseStore")

        val db = Firebase.firestore

        db.collection("Product")
            .add(product)
            .addOnSuccessListener { documentReference ->
                fireBaseDao.insert(
                    ProductFireBase(
                        idFireBase = documentReference.id,
                        description = product.description,
                        imgProduct = product.imgProduct,
                        brandProduct = product.brandProduct,
                        idCategoryFK = product.idCategoryFK,
                        ean = product.ean,
                        price = product.price,
                        date = dateFormat.format(Date())
                    )
                )
                StateInfo.Success( "FirestoreRepository DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                StateInfo.Error("FirestoreRepository Error adding document", e)
            }
    }

    override fun getFirebaseStore(
        scope: CoroutineScope,
        error: (Exception) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {

            val fireBase = fireBaseDao.getFireBaseIsDate(dateFormat.format(Date()))
            logE("isFireBase $fireBase")
            withContext(Dispatchers.Main) {
                if (!fireBase) {
                    logE("isFireBase2 OK Buscar")

                    val db = Firebase.firestore
                    db.collection("Product")
                        .get()
                        .addOnSuccessListener { res ->
                            val list = res.map { document ->
                                Log.d("FirestoreRepository", "${document.id} => ${document.data}")

                                return@map ProductFireBase(
                                    idProduct = 0,
                                    idFireBase = document.id,
                                    description = document.data["description"]?.toString() ?: "",
                                    imgProduct = document.data["imgProduct"]?.toString() ?: "",
                                    brandProduct = document.data["brandProduct"]?.toString() ?: "",
                                    ean = document.data["ean"]?.toString() ?: "",
                                    price = document.data["price"]?.toString()?.toFloat() ?: 0F,
                                    idCategoryFK = document.data["idCategoryFK"]?.toString()
                                        ?.toInt() ?: 0,
                                    date = dateFormat.format(Date())
                                )
                            }
                            logE("isFireBase3 OK list $list")

                            scope.launch(Dispatchers.IO) {
                                fireBaseDao.deleteAll()
                                fireBaseDao.insert(*list.toTypedArray())
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w("FirestoreRepository", "Error getting documents.", exception)
                            error(exception)
                        }
                }
            }
        }
    }

    override fun saveProductDataBase(product: ProductFireBase): StateInfo {
        return try {
            val productDescription = productDao.getProduct(product.description)
            if (productDescription == null) {
                val category = categoryDao.consultCategoryId(product.idCategoryFK).let { cat ->
                    cat
                        ?: categoryDao.consultCategory(CATEGORY_FIREBASE).let {
                            if (it != null) it
                            else {
                                categoryDao.insert(Category(nameCategory = CATEGORY_FIREBASE))
                                categoryDao.consultCategory(CATEGORY_FIREBASE)
                                    ?: Category(idCategory = 1, nameCategory = CATEGORY_FIREBASE)
                            }
                        }
                }
                productDao.insert(
                    Product(
                        description = product.description,
                        brandProduct = product.brandProduct,
                        idCategoryFK = category.idCategory,
                        ean = product.ean,
                        price = product.price,
                        categoryName = category.nameCategory
                    )
                )
                StateInfo.Success("Salvo com Sucesso")
            } else StateInfo.Error("Produto consta na base de dados")
        } catch (e: Exception) {
            StateInfo.Error("Erro ", e)
        }
    }
}