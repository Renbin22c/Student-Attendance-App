package com.renbin.student_attendance_app.ui.adapter

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.renbin.student_attendance_app.data.model.Student
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutNoteBinding


class StudentNoteAdapter(
    private var teachers: List<Teacher>,
    private var notes: List<Note>,
    private var students: List<Student>,
    private var user: FirebaseUser?
) : RecyclerView.Adapter<StudentNoteAdapter.NoteItemViewHolder>() {
    var listener: Listener? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentNoteAdapter.NoteItemViewHolder {
        val binding = ItemLayoutNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: StudentNoteAdapter.NoteItemViewHolder, position: Int) {
        val itemNote = notes[position]
        holder.bind(itemNote)
    }

    override fun getItemCount() = notes.size

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    fun setTeachers(teachers: List<Teacher>){
        this.teachers = teachers
        notifyDataSetChanged()
    }

    fun setStudents(students: List<Student>){
        this.students = students
        notifyDataSetChanged()
    }

    inner class NoteItemViewHolder(
        private val binding: ItemLayoutNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(note: Note) {
            binding.run {
                val filterStudent = note.student.contains(user?.uid)
                if (filterStudent) {
                    tvNote.text = note.title

                    val matchTeacher = teachers.find { it.id == note.createdBy }
                    tvNoteTeacher.text = matchTeacher?.name

                    cvNotes.setOnClickListener {
                        listener?.onClick(note)
                    }
                } else {
                    // If the student is not in the list, hide the entire view
                    itemView.visibility = View.GONE
                    itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
    }

    interface Listener {
        fun onClick(note: Note)
    }
}
