package com.guy.class26a_and_3

data class Score(
    val distance: Int,
    val coins: Int, // Added coins field
    val lat: Double,
    val lon: Double,
    val timestamp: Long = System.currentTimeMillis()
)