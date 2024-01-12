package com.renbin.student_attendance_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.core.util.NotificationUtil.createNotificationChannel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel(this)
    }
}