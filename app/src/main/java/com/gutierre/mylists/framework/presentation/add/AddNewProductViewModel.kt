package com.gutierre.mylists.framework.presentation.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.repository.CategoryRepository
import com.gutierre.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class AddNewProductViewModel(
    private val repository: ProductRepository,
    private val repositoryCategory: CategoryRepository
): ViewModel() {

    val categoryList: Flow<List<Category>> = repositoryCategory.consultCategoryList()

    val brandList: Flow<List<String>> = repository.getAllBrand()

    fun insertProduct(product: Product, quantity: String = "", returnInsert: (status: Boolean) -> Unit){
        viewModelScope.launch(Dispatchers.IO +  CoroutineExceptionHandler { _, _ -> }) {
            val status = repository.insertProduct(product)
            withContext(Dispatchers.Main){
                if (quantity.isNotBlank()) {
                    insertProductInShopping(product, quantity) {
                        returnInsert(status > 0L)
                    }
                }
                else returnInsert(status > 0L)
            }
        }
    }

    private fun insertProductInShopping(product: Product, quantity: String, returnInsert: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }) {
            val productDB = repository.getProduct(product.description)
            repository.insertShopping(
                shopping = ItemShopping(
                    idProductFK = productDB?.idProduct ?: product.idProduct,
                    quantity = quantity.toIntOrNull() ?: 1,
                    selected = false
                )
            )
            withContext(Dispatchers.Main){
                returnInsert()
            }
        }
    }

    fun consultCategory(name: String, response: (category: Category?) -> Unit){
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }){
            Log.e("consultCategory", " $name")
            val status = repositoryCategory.consultCategory(name)
            withContext(Dispatchers.Main){
                Log.e("consultCategory", " $status")
                if (status == null) insertCategory(category = Category(0, name)) {
                    response(it)
                }
                response(status)
            }
        }
    }

    private fun insertCategory(category: Category, response: (category: Category?) -> Unit){
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }){
            Log.e("insertCategory", " $category")
            val status = repositoryCategory.insertCategory(category)
            withContext(Dispatchers.Main){
                Log.e("insertCategory2", "status $status")
                if (status > 0) consultCategory(category.nameCategory) {
                    response(it)
                }
            }
        }
    }
}