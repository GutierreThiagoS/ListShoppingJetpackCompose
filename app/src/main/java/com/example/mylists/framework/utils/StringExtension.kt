package com.example.mylists.framework.utils

fun String?.notNull(): String {
    return this ?: ""
}

fun String?.toFloatNotNull(): Float {
    return try {
        if (!isNullOrBlank()) this.toFloat()
        else 0f
    } catch (e: Exception) {
        0f
    }
}

