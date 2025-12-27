package com.guy.class26a_and_3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guy.class26a_and_3.databinding.ItemScoreBinding

class ScoresAdapter(
    private val scores: List<Score>,
    private val onScoreClicked: (Score) -> Unit
) : RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.bind(score)
        holder.itemView.setOnClickListener { onScoreClicked(score) }
    }

    override fun getItemCount() = scores.size

    class ScoreViewHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Score) {
            binding.scoreDistance.text = "Distance: ${score.distance}"
        }
    }
}