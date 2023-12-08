package com.gutierre.mylists.domain.repository

import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.CategoryAndCountProduct
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun consultCategory(name: String): Category?

    fun consultCategoryList(): Flow<List<Category>>

    fun consultCategoryAndCountProductList(): Flow<List<CategoryAndCountProduct>>

    fun insertCategory(category: Category): Long

    fun removerCategoryCheckProducts(category: CategoryAndCountProduct): String

    fun editCategory(newName: String, category: CategoryAndCountProduct): String
}