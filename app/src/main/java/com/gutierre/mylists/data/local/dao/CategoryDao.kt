package com.gutierre.mylists.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gutierre.mylists.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg category: Category)

    @Delete
    fun delete(category: Category): Int

    @Query("SELECT * FROM Category WHERE nameCategory = :name Limit 1")
    fun consultCategory(name: String): Category?

    @Query("SELECT * FROM Category WHERE idCategory = :id Limit 1")
    fun consultCategoryId(id: Int): Category?

    @Query("SELECT * FROM Category ORDER BY nameCategory ASC")
    fun categoryList(): Flow<List<Category>>

    @Query("SELECT * FROM Category")
    fun getList(): List<Category>
}