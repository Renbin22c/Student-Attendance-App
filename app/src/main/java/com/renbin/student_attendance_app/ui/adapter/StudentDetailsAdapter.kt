package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentDetailsBinding

class StudentDetailsAdapter(
    private var students: List<Student>
): RecyclerView.Adapter<StudentDetailsAdapter.StudentDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentDetailsViewHolder {
        val binding = ItemLayoutStudentDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentDetailsViewHolder(binding)
    }

    override fun getItemCount() = students.size

    override fun onBindViewHolder(holder: StudentDetailsViewHolder, position: Int) {
        val itemStudentDetails = students[position]
        holder.bind(itemStudentDetails)
    }

    fun setStudentDetails(student: List<Student>){
        this.students = student
        notifyDataSetChanged()
    }

    inner class StudentDetailsViewHolder(
        private val binding: ItemLayoutStudentDetailsBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(student: Student){
            binding.run {
                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                tvStudentClass.text = student.classes
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date
            }
        }
    }
}