package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentAttendanceBinding

class StudentAttendanceAdapter(

): RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentAttendanceItemViewHolder {
        val binding = ItemLayoutStudentAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentAttendanceItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentAttendanceItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class StudentAttendanceItemViewHolder(
        private val binding: ItemLayoutStudentAttendanceBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(){

        }
    }
}