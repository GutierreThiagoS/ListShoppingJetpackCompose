package com.example.mylists.framework.presentation.to_do

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.domain.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ToDoViewModel(
    repository: ToDoRepository
): ViewModel() {

    val toDoListState: Flow<List<ToDoItem>> = repository.getToDoListFlow()

    fun insertToDo(title: String, description: String, dateFinal: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}