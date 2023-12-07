package com.gutierre.mylists.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gutierre.mylists.data.local.dao.CategoryDao
import com.gutierre.mylists.data.local.dao.ProductDao
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.state.StateInfo

class FireStoreRepositoryImp(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
): FireStoreRepository {

    companion object {
        const val CATEGORY_FIREBASE = "Compartilhado"
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
                StateInfo.Success( "FirestoreRepository DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                StateInfo.Error("FirestoreRepository Error adding document", e)
            }
    }

    override fun getProductsFirebaseStore(
        product: Product,
        result: () -> Unit,
        error: (Exception) -> Unit
    ) {
        val db = Firebase.firestore
        db.collection("Product")
            .get()
            .addOnSuccessListener { res ->
                if (res.none { product.description == it.data["description"] }) {
                    result()
                } else error(Exception("Produto Existe na Base de dados"))
                /*for (document in res) {
                    Log.d("FirestoreRepository", "${document.id} => ${document.data}")
                    logE("FirestoreRepository2  isExists ${product.description == document.data["description"]}")

                    if (product.description == document.data["description"]) {
                        error(Exception("Produto Existe na Base de dados"))
                        break
                    }
                }
                result()*/
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreRepository", "Error getting documents.", exception)
                error(exception)
            }
    }

    override fun getFirebaseStore(result: (List<Product>) -> Unit, error: (Exception) -> Unit) {
        val db = Firebase.firestore
        db.collection("Product")
            .get()
            .addOnSuccessListener { res ->
                val list = res.map  { document ->
                    Log.d("FirestoreRepository", "${document.id} => ${document.data}")

                    return@map Product(
                        idProduct = 0,
                        description = document.data["description"]?.toString() ?: "",
                        imgProduct = document.data["imgProduct"]?.toString() ?: "",
                        brandProduct = document.data["brandProduct"]?.toString() ?: "",
                        ean = document.data["ean"]?.toString() ?: "",
                        price = document.data["price"]?.toString()?.toFloat() ?: 0F,
                        idCategoryFK = document.data["idCategoryFK"]?.toString()?.toInt() ?: 0,
                    )
                }
                result(list.sortedBy { it.description })
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreRepository", "Error getting documents.", exception)
                error(exception)
            }
    }

    override fun saveProductDataBase(product: Product): StateInfo {
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