package com.renbin.student_attendance_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.databinding.ItemLayoutAddQuizBinding

class QuizAdapter(
    private var quizs :List<Quiz>
): RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    var listener: Listener? = null


    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemLayoutAddQuizBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun getItemCount() = quizs.size

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizs[position]
        if (holder is QuizViewHolder) {
            holder.bind(quiz)
        }
    }

    fun setQuiz(quizs: List<Quiz>) {
        this.quizs = quizs
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: ItemLayoutAddQuizBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.run {
                tvId.text = quiz.quizId
                tvTitle.text = quiz.title


                btnDelete.setOnClickListener {
                    listener?.onDelete(quiz)
                }
            }
        }
    }

    interface Listener {

        fun onDelete(quiz: Quiz)
    }
}

