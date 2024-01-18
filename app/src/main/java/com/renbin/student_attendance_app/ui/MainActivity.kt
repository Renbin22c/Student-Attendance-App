package com.renbin.student_attendance_app.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.service.NetworkManager
import com.renbin.student_attendance_app.core.util.MyBroadcastReceiver
import com.renbin.student_attendance_app.core.util.NotificationUtil.createNotificationChannel
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepoImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel(this)

        dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
            .setView(R.layout.network)
            .setCancelable(false)
            .create()

        val networkManager = NetworkManager(this)
        networkManager.observe(this) {
            if (!it) {
                if (!dialog.isShowing) dialog.show()
            } else {
                if (dialog.isShowing) dialog.hide()
            }
        }

        window.statusBarColor = Color.BLACK;

        MainScope().launch(Dispatchers.Main) {
            // Check if the user is logged in before scheduling notifications
            if (authService.getCurrentUser() != null) {
                val studentId = authService.getCurrentUser()
                val lessonRepo = LessonRepoImpl(authService)
                // Collect lessons from the flow and then schedule notifications
                LessonRepoImpl(authService).getAllLessons().collect { lessons ->
                    scheduleLessonNotifications(this@MainActivity, lessons, studentId)
                }
            }
        }
    }

    private fun scheduleLessonNotifications(context: Context, lessons: List<Lesson>, studentId: FirebaseUser?) {
        val currentTime = System.currentTimeMillis()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val userId = studentId?.uid // Get the UID of the FirebaseUser

        for (lesson in lessons) {
            // Check if the current student is associated with this lesson
            if (lesson.student.contains(userId)) { // Check if the list contains the user's ID
                val lessonStartTime = calculateLessonStartTime(lesson.date, lesson.time)

                // Check if the lesson is in the future
                if (currentTime < lessonStartTime) {
                    val intent = Intent(context, MyBroadcastReceiver::class.java)
                    intent.putExtra("title", lesson.name)
                    intent.putExtra("desc", "30 minutes until lesson starts")

                    val lessonId: Int = lesson.id?.hashCode() ?: 0
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        lessonId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    Log.d(
                        "LessonNotification",
                        "Scheduling notification for lesson: ${lesson.name}, time: $lessonStartTime"
                    )

                    // Check if the lesson is more than 30 minutes in the future
                    if (lessonStartTime - currentTime > 30 * 60 * 1000) {
                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            lessonStartTime - (30 * 60 * 1000), // 30 minutes before
                            pendingIntent
                        )
                    }
                }
            }
        }
    }

    private fun calculateLessonStartTime(date: String, time: String): Long {
        val dateTimeString = "$date $time"
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH)

        try {
            val lessonTime = dateFormat.parse(dateTimeString)
            return lessonTime?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }
}
