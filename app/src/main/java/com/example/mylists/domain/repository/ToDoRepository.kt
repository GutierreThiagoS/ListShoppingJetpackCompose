package com.example.mylists.domain.repository

import com.example.mylists.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    fun getToDoListFlow(): Flow<List<ToDoItem>>
}