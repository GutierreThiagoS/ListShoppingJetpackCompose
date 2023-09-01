package com.example.mylists.sealed_class

import com.example.mylists.domain.model.ProductOnItemShopping

sealed class BottomMenuState {
    object Loading : BottomMenuState()
    data class Success(
        val shoppingCart: List<ProductOnItemShopping>,
        val products: List<ProductOnItemShopping>,
    ) : BottomMenuState()
    data class Error(val message: String) : BottomMenuState()
}
