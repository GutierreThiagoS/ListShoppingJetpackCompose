package com.example.mylists.framework.utils

import android.util.Log

fun logE(message: String) {
    val messageSplit = ArrayList<String>(message.split(" "))
    if (messageSplit.size > 1) {
        val title = messageSplit.removeAt(0)
        Log.e(title,  messageSplit.joinToString(" "))
    } else Log.e("Error", message)
}