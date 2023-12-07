package com.gutierre.mylists.services

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.gutierre.mylists.App
import com.gutierre.mylists.domain.model.ToDoItem
import com.gutierre.mylists.framework.utils.logE

object FireBaseAnalytic {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(App.context)
    }

    fun createEvent(event: String, bundle: Bundle)  {
        logE("--- AnalyticsEventUseCase -- new incoming event: $event - bundle: $bundle")
        firebaseAnalytics.logEvent(event, bundle)
    }

    fun sendNotificationEvent(toDoItem: ToDoItem) {
        createEvent(
            "send_notification_today",
            Bundle().apply {
                putString("titulo", toDoItem.title)
                putString("menssagem", toDoItem.description)
                putInt("id", toDoItem.id)
                putInt("level", toDoItem.level)
            }
        )
    }
}