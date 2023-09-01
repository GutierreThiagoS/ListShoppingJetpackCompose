package com.example.mylists.framework.presentation.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class AddNewProductViewModel(
    private val repository: ProductRepository
): ViewModel() {

    val categoryList: Flow<List<Category>> = repository.consultCategoryList()

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
            val status = repository.consultCategory(name)
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
            val status = repository.insertCategory(category)
            withContext(Dispatchers.Main){
                Log.e("insertCategory2", "status $status")
                if (status > 0) consultCategory(category.nameCategory) {
                    response(it)
                }
            }
        }
    }
}