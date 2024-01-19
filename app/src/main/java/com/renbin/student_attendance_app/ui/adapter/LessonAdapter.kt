package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutLessonBinding
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentAttendanceBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// Define the LessonAdapter class extending RecyclerView.Adapter
class LessonAdapter(
    private var lessons: List<Lesson>,
    private var students: List<Student>,
    private var teachers: List<Teacher>,
    private var user: FirebaseUser?,
    private var currentFragment: String
): RecyclerView.Adapter<LessonAdapter.AttendanceItemViewHolder>() {
    var listener: Listener? = null

    // Override onCreateViewHolder to inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceItemViewHolder {
        val binding = ItemLayoutLessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttendanceItemViewHolder(binding)
    }

    // Override getItemCount to return the number of lessons
    override fun getItemCount() = lessons.size

    // Override onBindViewHolder to bind data to each item
    override fun onBindViewHolder(holder: AttendanceItemViewHolder, position: Int) {
        val itemLesson = lessons[position]
        holder.bind(itemLesson)
    }

    // Method to update lessons and notify the adapter
    fun setLessons(lessons: List<Lesson>){
        this.lessons = lessons
        notifyDataSetChanged()
    }

    // Method to update students and notify the adapter
    fun setStudents(students: List<Student>){
        this.students = students
        notifyDataSetChanged()
    }

    // Method to update teachers and notify the adapter
    fun setTeachers(teachers: List<Teacher>){
        this.teachers = teachers
        notifyDataSetChanged()
    }

    private fun checkDateForCheckIn(date: String): Boolean{
        val currentDate = formatDatestamp(System.currentTimeMillis())
        return  currentDate == date
    }

    private fun checkTimeForCheckIn(lessonTime: String): Boolean {
        // Get the current time
        val currentTime = Calendar.getInstance().time

        // Format the lesson time and current time using SimpleDateFormat
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedLessonTime = timeFormat.format(timeFormat.parse(lessonTime)!!)
        val formattedCurrentTime = timeFormat.format(currentTime)

        // Convert the formatted times to LocalTime objects
        val lessonLocalTime = LocalTime.parse(formattedLessonTime,
            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))
        val currentLocalTime = LocalTime.parse(formattedCurrentTime,
            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))

        // Calculate half an hour before the lesson time
        val halfHourBeforeLesson = lessonLocalTime.minusMinutes(31)

        // Check if the current time is between half an hour before the lesson time and the lesson time
        return currentLocalTime.isAfter(halfHourBeforeLesson) && currentLocalTime.isBefore(lessonLocalTime)
    }



    // Inner class for the ViewHolder
    inner class AttendanceItemViewHolder(
        private val binding: ItemLayoutLessonBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(lesson: Lesson){
            binding.run {
                // Bind lesson data to the UI elements
                tvLessonName.text = lesson.name
                tvLessonDetails.text = lesson.details
                tvDate.text = lesson.date
                tvTime.text = lesson.time
                tvClasses.text = lesson.classes

                // Find and display the teacher's name
                val matchTeacher = teachers.find { it.id == lesson.createdBy }
                tvLessonTeacher.text = matchTeacher?.name

                // Show delete button if the user created the lesson
                if(user?.uid == lesson.createdBy && currentFragment == "Lesson") ivDeleteLesson.visibility = View.VISIBLE

                if (checkDateForCheckIn(lesson.date) && checkTimeForCheckIn(lesson.time)){
                    // Show check-in button if the user is a student in the lesson
                    val filterStudent = lesson.student.contains(user?.uid)
                    if (filterStudent) {
                        ivCheckIn.visibility = View.VISIBLE
                    }
                }

                // Remove all previous views from llItems
                llItems.removeAllViews()

                // Iterate through students and display attendance information
                for(i in lesson.student.indices){
                    val attBinding = ItemLayoutStudentAttendanceBinding.inflate(
                        LayoutInflater.from(root.context),
                        llItems,
                        false
                    )
                    val matchStudent = students.find { it.id == lesson.student[i] }
                    attBinding.tvStudentName.text = matchStudent?.name ?: "Unknown"

                    if (i < lesson.attendance.size) {
                        // Set attendance status and update UI accordingly
                        if (lesson.attendance[i]) {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_check)
                            if(lesson.student[i] == user?.uid){
                                ivCheckIn.visibility =View.GONE
                            }
                        } else {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_close)
                        }
                    }

                    if (i < lesson.attendanceTime.size) {
                        // Display attendance time if available
                        attBinding.tvAttendanceTime.text = lesson.attendanceTime[i]
                    }

                    // Add the inflated view to llItems
                    llItems.addView(attBinding.root)
                }

                // Set click listener for check-in button
                ivCheckIn.setOnClickListener {
                    listener?.onClick(lesson.id.toString(), lesson)
                    ivCheckIn.visibility = View.GONE
                }

                // Set click listener for delete lesson button
                ivDeleteLesson.setOnClickListener {
                    listener?.onDelete(lesson)
                    ivDeleteLesson.visibility = View.GONE
                }
            }
        }
    }

    // Interface for the adapter's listener
    interface Listener {
        fun onClick(id: String, lesson: Lesson)
        fun onDelete(lesson: Lesson)
    }

}