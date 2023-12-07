package com.gutierre.mylists.framework.utils

import kotlinx.coroutines.CoroutineExceptionHandler

fun coroutineExceptionHandler(title: String = "ERROR"): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        try {
            logE("$title $throwable")
            throwable.printStackTrace()
            crashlyticsEvent(throwable)
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }
}