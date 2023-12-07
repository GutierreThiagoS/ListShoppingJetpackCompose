package com.gutierre.mylists.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gutierre.mylists.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg toDoItem: ToDoItem)

    @Update
    fun update(toDoItem: ToDoItem): Int

    @Delete
    fun delete(toDoItem: ToDoItem)

    @Query("UPDATE ToDoItem SET deleted = 1 WHERE id = :id")
    fun deleteId(id: Int)

    @Query("UPDATE ToDoItem SET concluded = 1 WHERE id = :id")
    fun readId(id: Int)

    @Query("""
        SELECT * FROM ToDoItem
        ORDER BY dateHour DESC
    """)
    fun getAllFlow(): Flow<List<ToDoItem>>

    @Query("""
        SELECT * FROM ToDoItem       
        WHERE deleted = 0
        AND concluded = 0 
        AND alert = 1
        ORDER BY dateHour DESC
    """)
    fun getToDoListNotifications(): List<ToDoItem>

    @Query("SELECT * FROM ToDoItem WHERE id = :id AND level = :level")
    fun getToDoItemInId(id: Int, level: Int): ToDoItem?

    @Query("""
        SELECT * FROM ToDoItem 
        WHERE deleted = 0
        AND concluded = 0 
        AND alert = 1
        ORDER BY dateHour DESC
    """)
    fun getToDoListFlowNotifications(): Flow<List<ToDoItem>>

}