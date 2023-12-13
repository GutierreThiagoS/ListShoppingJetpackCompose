package com.gutierre.mylists.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gutierre.mylists.domain.model.ProductFireBase
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductFireBaseDao {

    @Query("SELECT * FROM ProductFireBase ORDER BY description ASC")
    fun getAllFlow(): Flow<List<ProductFireBase>>

    @Query("SELECT * FROM ProductFireBase WHERE description = :description LIMIT 1")
    fun getProductInFireBase(description: String): ProductFireBase?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg fireBase: ProductFireBase)

    @Query("DELETE FROM ProductFireBase")
    fun deleteAll()

    @Query("SELECT COUNT(*) > 0 FROM ProductFireBase WHERE date == :date")
    fun getFireBaseIsDate(date: String): Boolean
}