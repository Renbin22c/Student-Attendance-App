package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutLessonBinding
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentAttendanceBinding

class LessonAdapter(
    private var lessons: List<Lesson>,
    private var students: List<Student>,
    private var teachers: List<Teacher>,
    private var user: FirebaseUser?
): RecyclerView.Adapter<LessonAdapter.AttendanceItemViewHolder>() {
    var listener: Listener? = null

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

    fun setTeachers(teachers: List<Teacher>){
        this.teachers = teachers
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

                val matchTeacher = teachers.find { it.id == lesson.createdBy }
                tvLessonTeacher.text = matchTeacher?.name

                if(user?.uid == lesson.createdBy) ivDeleteLesson.visibility = View.VISIBLE

                val filterStudent = lesson.student.filter {
                    it == user?.uid
                }

                if (filterStudent.isNotEmpty()) ivCheckIn.visibility = View.VISIBLE

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

                    if (i < lesson.attendance.size) {
                        if (lesson.attendance[i]) {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_check)
                            ivCheckIn.visibility = View.GONE
                        } else {
                            attBinding.ivAttendanceStatus.setImageResource(R.drawable.ic_close)
                        }
                    }

                    if (i < lesson.attendanceTime.size) {
                        attBinding.tvAttendanceTime.text = lesson.attendanceTime[i]
                    }

                    llItems.addView(attBinding.root)
                }

                ivCheckIn.setOnClickListener {
                    listener?.onClick(lesson.id.toString(), lesson)
                    ivCheckIn.visibility = View.GONE
                }

                ivDeleteLesson.setOnClickListener {
                    listener?.onDelete(lesson)
                    ivDeleteLesson.visibility = View.GONE
                }
            }
        }
    }

    interface Listener {
        fun onClick(id: String, lesson: Lesson)
        fun onDelete(lesson: Lesson)
    }

}