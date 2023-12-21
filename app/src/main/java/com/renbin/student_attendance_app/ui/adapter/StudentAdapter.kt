package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentBinding

class StudentAdapter(
    private var students: List<Student>
) : RecyclerView.Adapter<StudentAdapter.StudentItemViewHolder>(){
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentItemViewHolder {
        val binding = ItemLayoutStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentItemViewHolder(binding)
    }

    override fun getItemCount() = students.size

    override fun onBindViewHolder(holder: StudentItemViewHolder, position: Int) {
        val itemStudent = students[position]
        holder.bind(itemStudent)
    }

    fun setStudent(students: List<Student>){
        this.students = students
        notifyDataSetChanged()
    }

    inner class StudentItemViewHolder(
        private val binding: ItemLayoutStudentBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(student: Student){
            binding.run {
                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date
            }
        }
    }

    interface Listener{
        fun onClick(student: Student)
    }
}