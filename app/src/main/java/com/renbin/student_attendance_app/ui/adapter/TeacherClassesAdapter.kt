package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutClassesBinding

// Define TeacherClassesAdapter class extending RecyclerView.Adapter
class TeacherClassesAdapter(
    private var classes: List<Classes>,
    private var teachers: List<Teacher>
): RecyclerView.Adapter<TeacherClassesAdapter.ClassesItemViewHolder>() {
    // Initialize the listener variable
    var listener: Listener? = null

    // Inflate the layout for each teacher classes item
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherClassesAdapter.ClassesItemViewHolder {
        val binding = ItemLayoutClassesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassesItemViewHolder(binding)
    }

    // Bind data to the ViewHolder for each teacher classes item
    override fun onBindViewHolder(holder: TeacherClassesAdapter.ClassesItemViewHolder, position: Int) {
        val itemClasses = classes[position]
        holder.bind(itemClasses)
    }

    // Return the number of teacher classes
    override fun getItemCount() = classes.size

    // Update the classes list and notify the adapter
    fun setClasses(classes: List<Classes>){
        this.classes = classes
        notifyDataSetChanged()
    }

    // Update the teachers list and notify the adapter
    fun setTeachers(teachers: List<Teacher>){
        this.teachers = teachers
        notifyDataSetChanged()
    }

    // Inner class for the ViewHolder
    inner class ClassesItemViewHolder(
        private val binding: ItemLayoutClassesBinding
    ): RecyclerView.ViewHolder(binding.root){
        // Method to bind teacher classes data to the UI elements
        fun bind(classes: Classes){
            binding.run {
                tvClasses.text = classes.name

                // Find and display the teacher's name associated with the class
                val matchTeacher = teachers.find { it.id == classes.teacher }
                tvClassesTeacher.text = matchTeacher?.name

                // Set click listener for the item to handle clicks
                cvClasses.setOnClickListener {
                    listener?.onClick(classes)
                }

                // Set long click listener for the item to handle long clicks
                cvClasses.setOnLongClickListener {
                    ibDeleteClass.visibility =
                        if (ibDeleteClass.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE
                    true
                }

                // Set click listener for the delete icon to handle deletion
                ibDeleteClass.setOnClickListener {
                    listener?.onDelete(classes)
                    ibDeleteClass.visibility = View.GONE
                }
            }
        }
    }

    // Define a listener interface for handling clicks and long clicks
    interface Listener {
        fun onClick(classes: Classes)
        fun onDelete(classes: Classes)
    }
}