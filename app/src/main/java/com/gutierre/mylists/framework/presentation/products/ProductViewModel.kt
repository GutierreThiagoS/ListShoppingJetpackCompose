package com.gutierre.mylists.framework.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.domain.repository.FireStoreRepository
import com.gutierre.mylists.domain.repository.ProductRepository
import com.gutierre.mylists.framework.utils.coroutineExceptionHandler
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.state.StateInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class ProductViewModel(
    private val repository: ProductRepository,
    private val repositoryFireBase: FireStoreRepository
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

    fun saveProductInFireBase(productOnItemShopping: ProductOnItemShopping, state: (StateInfo) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("")) {

            val product = repository.getProductId(productOnItemShopping.idProduct)

            logE("productOnItemShopping $productOnItemShopping")

            withContext(Dispatchers.Main) {
                if (product != null) {
                    repositoryFireBase.getProductsFirebaseStore(
                        product,
                        result = {
                            repositoryFireBase.saveProductFirebaseStore(product) {
                                state(it)
                            }
                        },
                        error = {
                            state(StateInfo.Error(it.message ?: "Erro no Produto!", it))
                        }
                    )
                } else state(StateInfo.Error("Erro Produto não encontrado"))
            }
        }

    }
}