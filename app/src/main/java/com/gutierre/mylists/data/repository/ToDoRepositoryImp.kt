package com.gutierre.mylists.data.repository

import com.gutierre.mylists.data.local.dao.ToDoDao
import com.gutierre.mylists.domain.model.ToDoItem
import com.gutierre.mylists.domain.repository.ToDoRepository
import com.gutierre.mylists.framework.utils.hourFormat
import com.gutierre.mylists.framework.utils.isValidDate
import com.gutierre.mylists.framework.utils.logE
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

    override fun extendToDoItemId(id: Int, level: Int, result: (ToDoItem) -> Unit) {
        val item = toDoDao.getToDoItemInId(id, level)
        if (item != null && item.alert && !item.concluded && !item.deleted) {
            if (isValidDate(item.dateFinal)) {
                item.hourAlert = hourFormat.format(Date().time + (item.extendTimer * 60 * 1000))
                item.level++
                toDoDao.update(item)
                result(item)
            }
        }
    }

    override fun read(id: Int) {
        toDoDao.readId(id)
    }

    override fun getFindId(id: Int, level: Int): ToDoItem? {
        return toDoDao.getToDoItemInId(id, level)
    }

    override suspend fun refreshNotification() {
        val notifications = toDoDao.getToDoListNotifications()
        notifications.forEach {
            it.level ++
            toDoDao.update(it)
         }
    }
}