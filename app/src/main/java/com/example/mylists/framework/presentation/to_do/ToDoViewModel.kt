package com.example.mylists.framework.presentation.to_do

import androidx.lifecycle.ViewModel
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow

class ToDoViewModel(
    repository: ToDoRepository
): ViewModel() {

    val toDoListState: Flow<List<ToDoItem>> = repository.getToDoListFlow()

}