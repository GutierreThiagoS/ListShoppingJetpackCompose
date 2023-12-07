package com.gutierre.mylists.framework.utils

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

fun String.capitalizeDescription(): String {
    val strings = split(" ").map { text ->
        if (text.length > 3)
            text.mapIndexed { index, c ->  if (index == 0) c.uppercase() else c.lowercase() }.joinToString("")
        else text.lowercase()
    }
    return strings.joinToString(" ")
}

