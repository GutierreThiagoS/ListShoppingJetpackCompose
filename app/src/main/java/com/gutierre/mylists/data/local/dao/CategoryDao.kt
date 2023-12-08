package com.gutierre.mylists.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.domain.model.CategoryAndCountProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg category: Category)

    @Update
    fun update(category: Category): Int

    @Delete
    fun delete(category: Category): Int

    @Query("SELECT * FROM Category WHERE nameCategory = :name Limit 1")
    fun consultCategory(name: String): Category?

    @Query("SELECT * FROM Category WHERE idCategory = :id Limit 1")
    fun consultCategoryId(id: Int): Category?

    @Query("SELECT * FROM Category ORDER BY nameCategory ASC")
    fun categoryList(): Flow<List<Category>>

    @Query("""
        SELECT DISTINCT C.*, (
            SELECT COUNT(*) FROM Product P WHERE C.idCategory = P.idCategoryFK
        ) as countProduct FROM Category C 
        ORDER BY nameCategory ASC
    """)
    fun categoryAndCountProductList(): Flow<List<CategoryAndCountProduct>>

    @Query("SELECT * FROM Category")
    fun getList(): List<Category>
}