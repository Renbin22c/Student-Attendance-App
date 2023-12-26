package com.renbin.student_attendance_app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentDetailsEditBinding

// Define StudentDetailsEditAdapter class extending RecyclerView.Adapter
class StudentDetailsEditAdapter(
    private var students: List<Student>,
    private var classes: List<String>,
    private val context: Context
): RecyclerView.Adapter<StudentDetailsEditAdapter.StudentDetailsEditItemViewHolder>() {
    // Initialize the listener variable
    var listener: Listener? = null

    // Inflate the layout for each student details edit item
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentDetailsEditItemViewHolder {
        val binding = ItemLayoutStudentDetailsEditBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentDetailsEditItemViewHolder(binding)
    }

    // Return the number of students
    override fun getItemCount() = students.size

    // Bind data to the ViewHolder for each student details edit item
    override fun onBindViewHolder(holder: StudentDetailsEditItemViewHolder, position: Int) {
        val itemStudentDetailsEdit = students[position]
        holder.bind(itemStudentDetailsEdit)
    }

    // Update the student details edit list and notify the adapter
    fun setStudentDetailsEdit(student: List<Student>){
        this.students = student
        notifyDataSetChanged()
    }

    // Update the class list and notify the adapter
    fun setClass(classes: List<String>){
        this.classes = classes
        notifyDataSetChanged()
    }

    // Inner class for the ViewHolder
    inner class StudentDetailsEditItemViewHolder(
        private val binding: ItemLayoutStudentDetailsEditBinding
    ): RecyclerView.ViewHolder(binding.root){
        //  Method to bind student details edit data to the UI elements
        fun bind(student: Student){
            binding.run {
                var classSelect = student.classes

                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                // Format and display the creation date of the student details edit
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date

                // Set up the ArrayAdapter for the class selection dropdown
                val classAdapter = ArrayAdapter(
                    context,
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    classes
                )
                autoCompleteClass.setAdapter(classAdapter)
                // Set the initial value of the class selection dropdown
                autoCompleteClass.setText(classSelect, false)
                // Update the selected class when the user types or selects a value
                autoCompleteClass.addTextChangedListener {
                    classSelect = it.toString()
                }
                // Set click listeners for the update and delete buttons
                btnUpdate.setOnClickListener {
                    listener?.onUpdate(student, classSelect)
                }

                btnDelete.setOnClickListener {
                    listener?.onDelete(student)
                }
            }
        }
    }

    // Define a listener interface for handling updates and deletions
    interface Listener{
        fun onUpdate(student: Student, classes: String)
        fun onDelete(student: Student)
    }
}