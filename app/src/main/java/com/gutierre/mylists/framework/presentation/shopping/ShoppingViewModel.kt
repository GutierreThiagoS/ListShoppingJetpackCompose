package com.gutierre.mylists.framework.presentation.shopping

import androidx.lifecycle.ViewModel
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow

class ShoppingViewModel(
    repository: ShoppingRepository
): ViewModel() {

    var productOnShoppingState: Flow<List<ProductOnItemShopping>>  = repository.getList()

}