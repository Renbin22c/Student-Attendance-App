package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.databinding.ItemLayoutLessonBinding
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentAttendanceBinding

class LessonAdapter(
    private var lessons: List<Lesson>,
    private var students: List<Student>
): RecyclerView.Adapter<LessonAdapter.AttendanceItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceItemViewHolder {
        val binding = ItemLayoutLessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttendanceItemViewHolder(binding)
    }

    override fun getItemCount() = lessons.size

    override fun onBindViewHolder(holder: AttendanceItemViewHolder, position: Int) {
        val itemLesson = lessons[position]
        holder.bind(itemLesson)
    }

    fun setLessons(lessons: List<Lesson>){
        this.lessons = lessons
        notifyDataSetChanged()
    }

    fun setStudents(students: List<Student>){
        this.students = students
        notifyDataSetChanged()
    }


    inner class AttendanceItemViewHolder(
        private val binding: ItemLayoutLessonBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(lesson: Lesson){
            binding.run {
                tvLessonName.text = lesson.name
                tvLessonDetails.text = lesson.details
                tvDate.text = lesson.date
                tvTime.text = lesson.time
                tvClasses.text = lesson.classes
                llItems.removeAllViews()

                for(i in lesson.student.indices){
                    val attBinding = ItemLayoutStudentAttendanceBinding.inflate(
                        LayoutInflater.from(root.context),
                        llItems,
                        false
                    )
                    val matchStudent = students.find { it.id == lesson.student[i] }
                    attBinding.tvStudentName.text = matchStudent?.name ?: "Unknown"

                    // Check if the index is within the bounds of the other lists
                    if (i < lesson.attendance.size) {
                        if (lesson.attendance[i]) {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_check)
                        } else {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_close)
                        }
                    }

                    if (i < lesson.attendanceTime.size) {
                        attBinding.tvAttendanceTime.text = lesson.attendanceTime[i]
                    }

                    llItems.addView(attBinding.root)
                }
            }
        }
    }

}