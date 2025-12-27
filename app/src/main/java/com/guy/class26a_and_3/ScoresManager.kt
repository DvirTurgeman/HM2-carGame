package com.guy.class26a_and_3

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoresManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("scores_v2", Context.MODE_PRIVATE) // Changed file name to reset scores
    private val gson = Gson()

    fun saveScore(score: Score) {
        val scores = getScores().toMutableList()
        scores.add(score)
        scores.sortByDescending { it.distance }
        val topTen = scores.take(10)
        val json = gson.toJson(topTen)
        prefs.edit().putString("scores_list", json).apply()
    }

    fun getScores(): List<Score> {
        val json = prefs.getString("scores_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<Score>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}