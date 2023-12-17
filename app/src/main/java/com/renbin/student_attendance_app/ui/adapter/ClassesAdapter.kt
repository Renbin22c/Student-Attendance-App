package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.databinding.ItemLayoutClassesBinding

class ClassesAdapter(
    private var classes: List<Classes>
): RecyclerView.Adapter<ClassesAdapter.ClassesItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassesAdapter.ClassesItemViewHolder {
        val binding = ItemLayoutClassesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassesItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassesAdapter.ClassesItemViewHolder, position: Int) {
        val itemClasses = classes[position]
        holder.bind(itemClasses)
    }

    override fun getItemCount() = classes.size

    fun setClasses(classes: List<Classes>){
        this.classes = classes
        notifyDataSetChanged()
    }

    inner class ClassesItemViewHolder(
        private val binding: ItemLayoutClassesBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(classes: Classes){
            binding.run {
                tvClasses.text = classes.name
                tvClassesTeacher.text = classes.teacher
            }
        }
    }
}