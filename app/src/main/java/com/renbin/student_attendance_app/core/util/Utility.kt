package com.renbin.student_attendance_app.core.util

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utility {
    fun showToast(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun formatDatestamp(datestamp: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = datestamp
        return DateFormat.format("dd-MM-yyyy", cal).toString()
    }

    fun formatTimestamp(timestamp: Long): String{
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return when {
            hour > 12 -> String.format("%02d:%02d pm", hour - 12, minute)
            hour == 12 -> String.format("%02d:%02d pm", hour, minute)
            hour == 0 -> String.format("%02d:%02d am", hour + 12, minute)
            else -> String.format("%02d:%02d am", hour, minute)
        }
    }
}