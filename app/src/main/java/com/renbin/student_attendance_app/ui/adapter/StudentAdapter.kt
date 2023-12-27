package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentBinding

// Define StudentAdapter class extending RecyclerView.Adapter
class StudentAdapter(
    private var students: List<Student>
) : RecyclerView.Adapter<StudentAdapter.StudentItemViewHolder>(){

    // Inflate the layout for each student ite
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentItemViewHolder {
        val binding = ItemLayoutStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentItemViewHolder(binding)
    }

    // Return the number of students
    override fun getItemCount() = students.size

    // Bind data to the ViewHolder for each student
    override fun onBindViewHolder(holder: StudentItemViewHolder, position: Int) {
        val itemStudent = students[position]
        holder.bind(itemStudent)
    }

    // Update the student list and notify the adapter
    fun setStudent(students: List<Student>){
        this.students = students
        notifyDataSetChanged()
    }

    // Inner class for the ViewHolder
    inner class StudentItemViewHolder(
        private val binding: ItemLayoutStudentBinding
    ): RecyclerView.ViewHolder(binding.root){
        // Method to bind student data to the UI elements
        fun bind(student: Student){
            binding.run {
                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                // Format and display the creation date of the student
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date
            }
        }
    }
}