package com.gutierre.mylists.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessaging: FirebaseMessagingService() {
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("FireBaseMessaging", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // developer: Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FireBaseMessaging", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FireBaseMessaging", "Message data payload: ${remoteMessage.data}")

            // Check if data needs to be processed by long running job
            //if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob()
            //} else {
                // Handle message within 10 seconds
                //handleNow()
            //}
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FireBaseMessaging", "Message Notification Body: ${it.body}")
        }
    }
}