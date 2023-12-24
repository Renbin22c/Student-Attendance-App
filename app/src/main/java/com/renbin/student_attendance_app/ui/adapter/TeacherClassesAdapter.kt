package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutClassesBinding

class TeacherClassesAdapter(
    private var classes: List<Classes>,
    private var teachers: List<Teacher>
): RecyclerView.Adapter<TeacherClassesAdapter.ClassesItemViewHolder>() {
    var listener: Listener? = null

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

    override fun onBindViewHolder(holder: TeacherClassesAdapter.ClassesItemViewHolder, position: Int) {
        val itemClasses = classes[position]
        holder.bind(itemClasses)
    }

    override fun getItemCount() = classes.size

    fun setClasses(classes: List<Classes>){
        this.classes = classes
        notifyDataSetChanged()
    }

    fun setTeachers(teachers: List<Teacher>){
        this.teachers = teachers
        notifyDataSetChanged()
    }

    inner class ClassesItemViewHolder(
        private val binding: ItemLayoutClassesBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(classes: Classes){
            binding.run {
                tvClasses.text = classes.name

                val matchTeacher = teachers.find { it.id == classes.teacher }
                tvClassesTeacher.text = matchTeacher?.name

                cvClasses.setOnClickListener {
                    listener?.onClick(classes)
                }

                cvClasses.setOnLongClickListener {
                    ibDeleteClass.visibility =
                        if (ibDeleteClass.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE
                    true
                }

                ibDeleteClass.setOnClickListener {
                    listener?.onDelete(classes)
                    ibDeleteClass.visibility = View.GONE
                }
            }
        }
    }

    interface Listener {
        fun onClick(classes: Classes)
        fun onDelete(classes: Classes)
    }
}