package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.ItemLayoutNoteBinding

class NoteAdapter(
    private var teachers: List<Teacher>,
    private var notes: List<Note>,
): RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteAdapter.NoteItemViewHolder {
        val binding = ItemLayoutNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteItemViewHolder, position: Int) {
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

    inner class NoteItemViewHolder(
        private val binding: ItemLayoutNoteBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.run {
                tvNote.text = note.title

                val matchTeacher = teachers.find {it.id == note.createdBy }
                tvNoteTeacher.text = matchTeacher?.name

                cvNotes.setOnClickListener {
                    listener?.onClick(note)
                }
            }
        }
    }
    interface Listener {
        fun onClick(note: Note)
    }

}
