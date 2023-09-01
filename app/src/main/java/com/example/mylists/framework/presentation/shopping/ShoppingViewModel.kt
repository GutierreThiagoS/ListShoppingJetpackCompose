package com.example.mylists.framework.presentation.shopping

import androidx.lifecycle.ViewModel
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow

class ShoppingViewModel(
    private val repository: ShoppingRepository
): ViewModel() {

    var productOnShoppingState: Flow<List<ProductOnItemShopping>>  = repository.getList()

    fun editPriceProduct(productOnItemShopping: ProductOnItemShopping): Int{
        return repository.editPriceProduct(productOnItemShopping)
    }

}