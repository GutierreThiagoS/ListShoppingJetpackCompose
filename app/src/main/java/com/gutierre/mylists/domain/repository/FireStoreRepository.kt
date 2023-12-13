package com.gutierre.mylists.domain.repository

import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductFireBase
import com.gutierre.mylists.state.StateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FireStoreRepository {

    fun getFireBaseAllFlow(): Flow<List<ProductFireBase>>

    fun getProductFireBaseInRoom(description: String): ProductFireBase?

    fun saveProductFirebaseStore(
        product: Product,
        result: (StateInfo) -> Unit
    )

    fun getFirebaseStore(
        scope: CoroutineScope,
        error: (Exception) -> Unit
    )

    fun saveProductDataBase(product: ProductFireBase): StateInfo

}