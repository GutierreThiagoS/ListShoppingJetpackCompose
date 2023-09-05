package com.example.mylists.framework.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.domain.repository.ProductRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class ProductViewModel(
    private val repository: ProductRepository
): ViewModel() {

    val productListState: Flow<List<ProductOnItemShopping>> = repository.productList()

    fun insertProductInShoppingList(product: ProductOnItemShopping){
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }){
            repository.insertShopping(
                shopping = ItemShopping(
                    product.idItem, product.idProduct, product.quantity, product.selected
                )
            )
        }
    }

    fun removeProduct(product: ProductOnItemShopping, alert: (message: String) -> Unit){
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }) {
            val message = repository.removerProduct(product)
            withContext(Dispatchers.Main) {
                alert(message)
            }
        }
    }
}