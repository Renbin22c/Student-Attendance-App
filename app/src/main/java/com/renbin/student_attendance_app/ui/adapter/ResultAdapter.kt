package com.renbin.student_attendance_app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renbin.student_attendance_app.data.model.Result
import com.renbin.student_attendance_app.databinding.ItemLayoutLeaderboardBinding

class ResultAdapter(
    private var result: List<Result>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutLeaderboardBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun getItemCount() = result.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = result[position]
        if (holder is ResultAdapter.ResultViewHolder) {
            holder.bind(result)
        }
    }

    fun setResult(result: List<Result>) {
        this.result = result
        notifyDataSetChanged()

    }

    inner class ResultViewHolder(
        private val binding: ItemLayoutLeaderboardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.run {
                Log.d("debugging", result.toString())
                tvUsername.text = result.name
                tvScore.text = result.result
                tvQuizId.text = result.quizId
            }
        }
    }

    interface Listener{
        fun onClick(result: Result)
    }
}