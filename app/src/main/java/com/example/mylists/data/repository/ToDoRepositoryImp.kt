package com.example.mylists.data.repository

import com.example.mylists.data.local.dao.ToDoDao
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.domain.repository.ToDoRepository
import com.example.mylists.framework.utils.hourFormat
import com.example.mylists.framework.utils.isValidDate
import com.example.mylists.framework.utils.logE
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ToDoRepositoryImp(
    private val toDoDao: ToDoDao
): ToDoRepository {
    override fun getToDoListFlow(): Flow<List<ToDoItem>> {
        return toDoDao.getAllFlow()
    }

    override fun getToDoListFlowNotifications(): Flow<List<ToDoItem>> {
        return toDoDao.getToDoListFlowNotifications()
    }

    override fun insertOrUpdateToDoList(toDoItem: ToDoItem) {
        logE("updateToDoList $toDoItem")
        if (toDoDao.update(toDoItem) == 0) toDoDao.insert(toDoItem)
    }

    override fun deleteId(id: Int) {
        toDoDao.deleteId(id)
    }

    override fun deleteItem(toDoItem: ToDoItem) {
        toDoDao.delete(toDoItem)
    }

    override fun extendToDoItemId(id: Int, result: (ToDoItem) -> Unit) {
        val item = toDoDao.getToDoItemInId(id)
        if (item != null && item.alert && !item.concluded && !item.deleted) {
            if (isValidDate(item.dateFinal)) {
                item.hourAlert = hourFormat.format(Date().time + (5 * 60 * 1000))
                toDoDao.update(item)
                result(item)
            }
        }
    }

    override fun read(id: Int) {
        toDoDao.readId(id)
    }

    override fun getFindId(id: Int): ToDoItem? {
        return toDoDao.getToDoItemInId(id)
    }
}