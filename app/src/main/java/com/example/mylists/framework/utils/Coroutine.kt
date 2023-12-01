package com.example.mylists.framework.utils

import kotlinx.coroutines.CoroutineExceptionHandler

fun coroutineExceptionHandler(title: String = "ERROR"): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        try {
            logE("$title $throwable")
            throwable.printStackTrace()
//            SafeCrashlytics.logException(throwable)
        } catch (e: UnsatisfiedLinkError) {
            e.printStackTrace()
        }
    }
}