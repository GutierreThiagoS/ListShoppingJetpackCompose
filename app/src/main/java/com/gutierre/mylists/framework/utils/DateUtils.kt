package com.gutierre.mylists.framework.utils

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
        logE("isValidDateHour Date(${Date().time }).time < parseDateToDo.time(${parseDateToDo.time}) ${Date().time < parseDateToDo.time}")
        Date().time < parseDateToDo.time
    } ?: false

}

fun valueValidDateHour(dateHour: String): Long {
    val dateHourFormat = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.getDefault())

    return dateHourFormat.parse(dateHour)?.let { parseDateToDo ->
        logE("valueValidDateHour parseDateToDo.time - Date().time  ${parseDateToDo.time - Date().time}")
        logE("valueValidDateHour segundos  ${(parseDateToDo.time - Date().time)/1000}")
        logE("valueValidDateHour minutos  ${(parseDateToDo.time - Date().time)/1000/60}")
        (parseDateToDo.time - Date().time)/1000/60
    } ?: 0
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
