package com.gutierre.mylists.domain.repository

import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.state.StateProductBarCode
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getList(): Flow<List<ProductOnItemShopping>>

    fun getTotal(): Flow<Float?>

    fun update(itemShopping: ItemShopping): Int

    fun navigationBadgeCount(title: String): Flow<Int?>

    fun checkProduct()

    suspend fun getProductInBarCode(barcode: String, company: Int = 2): StateProductBarCode

    suspend fun getShoppingProductId(productId: Int): ItemShopping?
}