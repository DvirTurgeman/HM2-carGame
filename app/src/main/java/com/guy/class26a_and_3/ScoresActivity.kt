package com.guy.class26a_and_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guy.class26a_and_3.databinding.ActivityScoresBinding

class ScoresActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoresBinding
    private lateinit var scoresManager: ScoresManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoresManager = ScoresManager(this)

        // Load all scores onto the map initially
        val scores = scoresManager.getScores()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
        mapFragment?.showAllScoresOnMap(scores)

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    // This function is called by the ScoresListFragment when an item is clicked
    fun onScoreClicked(score: Score) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? MapFragment
        mapFragment?.focusOnLocation(score.lat, score.lon)
    }
}