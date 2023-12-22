package com.renbin.student_attendance_app.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.renbin.student_attendance_app.R

object NotificationUtil {
    const val NOTIFICATION_CHANNEL_ID = "lesson_notification_channel"
    const val NOTIFICATION_NAME = "lesson_notification"
    const val NOTIFICATION_ID = 123

    fun createNotificationChannel(context: Context){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotificationBuilder(context: Context, title: String, desc: String): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
    }

    fun notify(context: Context, notification: Notification){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

}