package com.example.mylists.framework.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("ConstantLocale")
val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

@SuppressLint("ConstantLocale")
val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

fun isValidDateHour(dateHour: String): Boolean {
    val dateHourFormat = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.getDefault())

    return dateHourFormat.parse(dateHour)?.let { parseDateToDo ->

        logE("isValidDateHour Date() ${Date()}, parseDateToDo $parseDateToDo")
        logE("isValidDateHour Date().time < parseDateToDo.time ${Date().time < parseDateToDo.time}")
        Date().time < parseDateToDo.time
    } ?: false

}

fun isValidDate(date: String): Boolean {
    val dateHourFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return dateHourFormat.parse(date+"06:00")?.let { parseDateToDo ->

        logE("isValidDate Date() ${Date()}, parseDateToDo $parseDateToDo")
        logE("isValidDate Date() ${dateHourFormat.format(Date())}")
        logE("isValidDate Date(${Date().time}).time < parseDateToDo.time(${parseDateToDo.time}) ${Date().time < parseDateToDo.time}")
        Date().time <= parseDateToDo.time
    } ?: false

}

fun isValidDateToday(date: String): Boolean {
    return dateFormat.format(Date()) == date
}
