package com.example.mylists.domain.model

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
    val level: Int,
    val alert: Boolean,
    val concluded: Boolean,
    val deleted: Boolean,
)
