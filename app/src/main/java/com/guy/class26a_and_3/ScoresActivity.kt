package com.guy.class26a_and_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.guy.class26a_and_3.databinding.ActivityScoresBinding

class ScoresActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoresBinding
    private lateinit var scoresManager: ScoresManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoresManager = ScoresManager(this)
        val scores = scoresManager.getScores()

        // Pass scores to MapFragment using a Bundle
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
        val bundle = Bundle()
        bundle.putString("scores_json", Gson().toJson(scores))
        mapFragment?.arguments = bundle

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    fun onScoreClicked(score: Score) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
        mapFragment?.focusOnLocation(score.lat, score.lon)
    }
}