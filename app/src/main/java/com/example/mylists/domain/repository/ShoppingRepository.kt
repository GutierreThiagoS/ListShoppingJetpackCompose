package com.example.mylists.domain.repository

import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.ProductOnItemShopping
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getList(): Flow<List<ProductOnItemShopping>>

    fun getTotal(): Flow<Float?>

    fun update(itemShopping: ItemShopping): Int

    fun editPriceProduct(productOnItemShopping: ProductOnItemShopping): Int

    fun navigationBadgeCount(title: String): Flow<Int?>
    fun checkProduct()
    suspend fun getProductInBarCode(barcode: String): List<ProductOnItemShopping>
}