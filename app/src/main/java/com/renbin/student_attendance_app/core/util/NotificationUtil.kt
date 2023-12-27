package com.renbin.student_attendance_app.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.renbin.student_attendance_app.R

// Define a NotificationUtil object with functions for creating and displaying notifications
object NotificationUtil {

    // Constants for notification channel
    const val NOTIFICATION_CHANNEL_ID = "lesson_notification_channel"
    const val NOTIFICATION_NAME = "lesson_notification"
    const val NOTIFICATION_ID = 123

    // Function to create the notification channel
    fun createNotificationChannel(context: Context){
        // Create a notification channel with specified ID, name, and importance level
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        // Get the notification manager from the system service
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel
        notificationManager.createNotificationChannel(channel)
    }

    // Function to create a notification builder with specified title and description
    fun createNotificationBuilder(context: Context, title: String, desc: String): NotificationCompat.Builder{
        // Log the title and description for debugging purposes
        Log.d("debugging", "$title, $desc")

        // Return a notification builder with specified properties
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
    }

    // Function to display a notification
    fun notify(context: Context, notification: Notification){
        // Get the notification manager from the system service
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notify with the specified notification ID
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

}
