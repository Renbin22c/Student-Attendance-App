package com.renbin.student_attendance_app.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.ItemLayoutStudentDetailsEditBinding

class StudentDetailsEditAdapter(
    private var students: List<Student>,
    private var classes: List<String>,
    private val context: Context
): RecyclerView.Adapter<StudentDetailsEditAdapter.StudentDetailsEditItemViewHolder>() {
    var listener: Listener? = null

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

    override fun getItemCount() = students.size

    override fun onBindViewHolder(holder: StudentDetailsEditItemViewHolder, position: Int) {
        val itemStudentDetailsEdit = students[position]
        holder.bind(itemStudentDetailsEdit)
    }

    fun setStudentDetailsEdit(student: List<Student>){
        this.students = student
        notifyDataSetChanged()
    }

    fun setClass(classes: List<String>){
        this.classes = classes
        notifyDataSetChanged()
    }

    inner class StudentDetailsEditItemViewHolder(
        private val binding: ItemLayoutStudentDetailsEditBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(student: Student){
            binding.run {
                var classSelect = student.classes

                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                val date = formatDatestamp(student.createdAt)
                tvStudentCreatedAt.text = date

                val classAdapter = ArrayAdapter(
                    context,
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    classes
                )
                autoCompleteClass.setAdapter(classAdapter)

                autoCompleteClass.setText(classSelect, false)

                autoCompleteClass.addTextChangedListener {
                    classSelect = it.toString()
                }

                btnUpdate.setOnClickListener {
                    listener?.onUpdate(student, classSelect)
                }

                btnDelete.setOnClickListener {
                    listener?.onDelete(student)
                }
            }
        }
    }

    interface Listener{
        fun onUpdate(student: Student, classes: String)
        fun onDelete(student: Student)
    }
}