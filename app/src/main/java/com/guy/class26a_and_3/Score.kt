package com.guy.class26a_and_3

data class Score(
    val distance: Int,
    val lat: Double,
    val lon: Double,
    val timestamp: Long = System.currentTimeMillis()
)