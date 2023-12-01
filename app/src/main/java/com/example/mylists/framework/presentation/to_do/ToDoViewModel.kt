package com.example.mylists.framework.presentation.to_do

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.domain.repository.ToDoRepository
import com.example.mylists.framework.utils.coroutineExceptionHandler
import com.example.mylists.framework.utils.dateFormat
import com.example.mylists.framework.utils.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoViewModel(
    private val repository: ToDoRepository
): ViewModel() {

    val toDoListState: Flow<List<ToDoItem>> = repository.getToDoListFlow()

    fun insertOrUpdateToDoItem(
        itemOld: ToDoItem?,
        title: String,
        description: String,
        dateFinal: String,
        hourAlertValue: String,
        isAlert: Boolean,
        returnError: (Throwable) -> Unit,
        returnSuccess: () -> Unit
    ) {
        val dateHourParse = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.getDefault())
        val dateHourFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())

        val dateHour = dateHourParse.parse(dateFinal + hourAlertValue)?.let {
            logE("TESTE DATA $it")
            logE("TESTE dateHourFormat.format(it) ${dateHourFormat.format(it)}")
            dateHourFormat.format(it).toLongOrNull() ?: 0L
        } ?: 0L

        logE("ToDoItem itemOld $itemOld")
        logE("TESTE dateHour $dateHour")
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("insertToDo")) {
            repository.insertOrUpdateToDoList(
                toDoItem = ToDoItem(
                    id = itemOld?.id ?: 0,
                    title = title,
                    description = description,
                    dateCreate = itemOld?.dateCreate ?: dateFormat.format(Date()),
                    dateUpdate = dateFormat.format(Date()),
                    dateFinal = dateFinal,
                    hourAlert = hourAlertValue,
                    hourInitAlert = hourAlertValue,
                    dateHour = dateHour,
                    alert = isAlert
                )
            )
        }.invokeOnCompletion {
            logE("invokeOnCompletion E $it")
            viewModelScope.launch(Dispatchers.Main) {
                if (it != null) returnError(it)
                else returnSuccess.invoke()
            }
        }
    }

    fun deleteToDoItemId(id: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("insertToDo")) {
            repository.deleteId(id)
        }
    }

    fun deleteToDoItem(toDoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("insertToDo")) {
            repository.deleteItem(toDoItem)
        }
    }

    fun extendToDoItemId(id: Int, result: (ToDoItem) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("insertToDo")) {
            repository.extendToDoItemId(id, result)
        }
    }

    fun getToDoItemId(id: Int?, result: (ToDoItem?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler("insertToDo")) {
            val item = id?.let { repository.getFindId(it) }
            withContext(Dispatchers.Main + coroutineExceptionHandler()) {
                result(item)
            }
        }
    }
}