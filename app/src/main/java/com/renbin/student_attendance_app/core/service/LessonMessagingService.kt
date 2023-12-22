package com.renbin.student_attendance_app.core.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.renbin.student_attendance_app.core.util.NotificationUtil

class LessonMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification != null){
            val notification =
                NotificationUtil.createNotificationBuilder(
                    this,
                    message.notification?.title?:"Message from Firebase",
                    message.notification?.body?:"Testing push notification"
                ).build()
            NotificationUtil.notify(this, notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("debugging", " Service: $token")
    }
}