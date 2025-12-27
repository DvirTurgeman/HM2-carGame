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
        holder.bind(score, position + 1) // Pass the rank to the holder
        holder.itemView.setOnClickListener { onScoreClicked(score) }
    }

    override fun getItemCount() = scores.size

    class ScoreViewHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Score, rank: Int) {
            binding.scoreRank.text = "#$rank"
            binding.scoreDistance.text = "Distance: ${score.distance}"
            binding.scoreCoins.text = "Coins: ${score.coins}"
        }
    }
}