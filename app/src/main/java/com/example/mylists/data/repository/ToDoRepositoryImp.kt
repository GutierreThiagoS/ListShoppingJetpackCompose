package com.example.mylists.data.repository

import com.example.mylists.data.local.dao.ToDoDao
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow

class ToDoRepositoryImp(
    private val toDoDao: ToDoDao
): ToDoRepository {
    override fun getToDoListFlow(): Flow<List<ToDoItem>> {
        return toDoDao.getAllFlow()
    }
}