package com.example.mylists.domain.repository

import com.example.mylists.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    fun getToDoListFlow(): Flow<List<ToDoItem>>

    fun getToDoListFlowNotifications(): Flow<List<ToDoItem>>

    fun insertOrUpdateToDoList(toDoItem: ToDoItem)

    fun deleteId(id: Int)

    fun deleteItem(toDoItem: ToDoItem)

    fun extendToDoItemId(id: Int, result: (ToDoItem) -> Unit)

    fun read(id: Int)

    fun getFindId(id: Int): ToDoItem?
}