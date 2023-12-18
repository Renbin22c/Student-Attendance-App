package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.databinding.ItemLayoutLessonBinding
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentAttendanceBinding

class LessonAdapter(
    private val lessons: List<Lesson>
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

    inner class AttendanceItemViewHolder(
        private val binding: ItemLayoutLessonBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(lesson: Lesson){
            binding.run {
                tvLessonName.text = lesson.name
                tvLessonDetails.text = lesson.details
                tvDate.text = lesson.date
            }
        }
    }

}