package com.example.mylists.domain.repository

import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.sealed_class.BottomMenuState
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getList(): Flow<List<ProductOnItemShopping>>

    fun getTotal(): Flow<Float?>

    fun update(itemShopping: ItemShopping): Int

    fun editPriceProduct(productOnItemShopping: ProductOnItemShopping): Int

    suspend fun sizeListsProdAndShopping(): BottomMenuState

    fun navigationBadgeCount(title: String): Flow<Int?>
}