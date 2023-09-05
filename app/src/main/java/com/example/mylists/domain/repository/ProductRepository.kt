package com.example.mylists.domain.repository

import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ItemShopping
import com.example.mylists.domain.model.Product
import com.example.mylists.domain.model.ProductOnItemShopping
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun insertProduct(product: Product): Long

    fun productList(): Flow<List<ProductOnItemShopping>>

    fun getProduct(description: String): Product?

    fun consultCategory(name: String): Category?

    fun consultCategoryList(): Flow<List<Category>>

    fun getAllBrand(): Flow<List<String>>

    fun insertCategory(category: Category): Long

    fun insertShopping(shopping: ItemShopping): ItemShopping?

    fun removerProduct(product: ProductOnItemShopping): String
}