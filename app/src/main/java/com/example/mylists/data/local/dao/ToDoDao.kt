package com.example.mylists.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mylists.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg toDoItem: ToDoItem)

    @Update
    fun update(toDoItem: ToDoItem): Int

    @Query("""
        SELECT * FROM ToDoItem
    """)
    fun getAllFlow(): Flow<List<ToDoItem>>

}