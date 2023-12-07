package com.gutierre.mylists.framework.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun crashlyticsEvent(throwable: Throwable){
    try{
        logE("SafeCrashlytics - throwable = $throwable")
        FirebaseCrashlytics.getInstance().recordException(throwable)
    } catch (e: Exception){
        e.printStackTrace()
    }
}