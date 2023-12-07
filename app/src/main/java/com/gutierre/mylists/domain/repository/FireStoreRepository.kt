package com.gutierre.mylists.domain.repository

import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.state.StateInfo

interface FireStoreRepository {

    fun saveProductFirebaseStore(
        product: Product,
        result: (StateInfo) -> Unit
    )

    fun getProductsFirebaseStore(
        product: Product,
        result: () -> Unit,
        error: (Exception) -> Unit
    )

    fun getFirebaseStore(
        result: (List<Product>) -> Unit,
        error: (Exception) -> Unit
    )

    fun saveProductDataBase(product: Product): StateInfo

}