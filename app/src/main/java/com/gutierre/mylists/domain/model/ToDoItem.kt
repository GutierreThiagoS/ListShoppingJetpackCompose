package com.gutierre.mylists.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dateCreate: String,
    val dateUpdate: String,
    val dateFinal: String,
    var hourAlert: String,
    val hourInitAlert: String,
    val dateHour: Long,
    var level: Int = 1,
    val extendTimer: Int = 15,
    val alert: Boolean,
    val concluded: Boolean = false,
    val deleted: Boolean = false,
)
