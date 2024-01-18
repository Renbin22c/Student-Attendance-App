package com.renbin.student_attendance_app.core.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.renbin.student_attendance_app.ui.MainActivity

class MyBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, data: Intent?) {

        if (context == null){
            return
        } else {
            val mainActivityIntent = Intent(context, MainActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val notificationBuilder =
                NotificationUtil.createNotificationBuilder(
                    context,
                    data?.getStringExtra("title") ?: "Title",
                    data?.getStringExtra("desc") ?: "Description"
                )

            notificationBuilder.setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    mainActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            NotificationUtil.notify(context, notificationBuilder.build())
        }
    }

}