package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentDetailsBinding

// Define StudentDetailsAdapter class extending RecyclerView.Adapter
class StudentDetailsAdapter(
    private var students: List<Student>
): RecyclerView.Adapter<StudentDetailsAdapter.StudentDetailsViewHolder>() {

    //Inflate the layout for each student details item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentDetailsViewHolder {
        val binding = ItemLayoutStudentDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentDetailsViewHolder(binding)
    }

    // Return the number of students
    override fun getItemCount() = students.size

    // Bind data to the ViewHolder for each student details item
    override fun onBindViewHolder(holder: StudentDetailsViewHolder, position: Int) {
        val itemStudentDetails = students[position]
        holder.bind(itemStudentDetails)
    }

    // Update the student details list and notify the adapter
    fun setStudentDetails(student: List<Student>){
        this.students = student
        notifyDataSetChanged()
    }

    // Inner class for the ViewHolder
    inner class StudentDetailsViewHolder(
        private val binding: ItemLayoutStudentDetailsBinding
    ): RecyclerView.ViewHolder(binding.root){
        // Method to bind student details data to the UI elements
        fun bind(student: Student){
            binding.run {
                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                tvStudentClass.text = student.classes

                // Format and display the creation date of the student details
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date
            }
        }
    }
}