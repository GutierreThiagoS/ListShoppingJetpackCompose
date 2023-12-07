package com.gutierre.mylists.domain.repository

import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.ItemShopping
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun insertProduct(product: Product): Long

    fun productList(): Flow<List<ProductOnItemShopping>>

    fun getProduct(description: String): Product?

    fun getProductId(id: Int): Product?

    fun consultCategory(name: String): Category?

    fun consultCategoryList(): Flow<List<Category>>

    fun getAllBrand(): Flow<List<String>>

    fun insertCategory(category: Category): Long

    fun insertShopping(shopping: ItemShopping): ItemShopping?

    fun removerProduct(product: ProductOnItemShopping): String

    fun removerCategoryCheckProducts(category: Category): String

}