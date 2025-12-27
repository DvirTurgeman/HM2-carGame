package com.guy.class26a_and_3

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guy.class26a_and_3.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private var delay = 1000L
    private var isGameRunning = false

    private var carPosition = 2
    private var lives = 3
    private var coins = 0
    private var distance = 0

    private val obstaclesMatrix = Array(5) { IntArray(5) { 0 } } // 0 = empty, 1 = rock, 2 = coin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        startGame()
    }

    private fun initViews() {
        binding.btnLeft.setOnClickListener { moveCar(-1) }
        binding.btnRight.setOnClickListener { moveCar(1) }

        updateCarUI()
        updateHeartsUI()
        updateCountersUI()
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (isGameRunning) {
                tick()
                handler.postDelayed(this, delay)
            }
        }
    }

    private fun startGame() {
        isGameRunning = true
        handler.postDelayed(runnable, delay)
    }

    private fun stopGame() {
        isGameRunning = false
        handler.removeCallbacks(runnable)
    }

    private fun tick() {
        distance++

        for (row in 4 downTo 1) {
            for (col in 0..4) {
                obstaclesMatrix[row][col] = obstaclesMatrix[row - 1][col]
            }
        }

        for (col in 0..4) {
            obstaclesMatrix[0][col] = 0
        }

        val generateType = Random.nextInt(0, 10) // 0-6 for obstacle, 7-9 for coin
        val randomLane = Random.nextInt(0, 5)

        if (generateType < 7) { // Generate obstacle
            obstaclesMatrix[0][randomLane] = 1
        } else { // Generate coin
            obstaclesMatrix[0][randomLane] = 2
        }

        updateObstaclesUI()
        checkCollision()
        updateCountersUI()
    }

    private fun checkCollision() {
        when (obstaclesMatrix[4][carPosition]) {
            1 -> crash()
            2 -> collectCoin()
        }
    }

    private fun crash() {
        lives--
        updateHeartsUI()
        Toast.makeText(this, "CRASH!", Toast.LENGTH_SHORT).show()
        vibrate()
        obstaclesMatrix[4][carPosition] = 0
        updateObstaclesUI()

        if (lives == 0) {
            Toast.makeText(this, "GAME OVER! Restarting...", Toast.LENGTH_LONG).show()
            lives = 3
            coins = 0
            distance = 0
            updateHeartsUI()
            updateCountersUI()
            for (i in 0..4) {
                for (j in 0..4) {
                    obstaclesMatrix[i][j] = 0
                }
            }
            updateObstaclesUI()
        }
    }

    private fun collectCoin() {
        coins++
        obstaclesMatrix[4][carPosition] = 0
        updateObstaclesUI()
        updateCountersUI()
    }

    private fun moveCar(direction: Int) {
        carPosition += direction
        if (carPosition < 0) carPosition = 0
        if (carPosition > 4) carPosition = 4
        updateCarUI()
        checkCollision()
    }

    private fun updateCarUI() {
        binding.imgCar0.visibility = View.INVISIBLE
        binding.imgCar1.visibility = View.INVISIBLE
        binding.imgCar2.visibility = View.INVISIBLE
        binding.imgCar3.visibility = View.INVISIBLE
        binding.imgCar4.visibility = View.INVISIBLE

        when (carPosition) {
            0 -> binding.imgCar0.visibility = View.VISIBLE
            1 -> binding.imgCar1.visibility = View.VISIBLE
            2 -> binding.imgCar2.visibility = View.VISIBLE
            3 -> binding.imgCar3.visibility = View.VISIBLE
            4 -> binding.imgCar4.visibility = View.VISIBLE
        }
    }

    private fun updateObstaclesUI() {
        val viewsMatrix = arrayOf(
            arrayOf(binding.imgCoin00, binding.imgCoin01, binding.imgCoin02, binding.imgCoin03, binding.imgCoin04),
            arrayOf(binding.imgCoin10, binding.imgCoin11, binding.imgCoin12, binding.imgCoin13, binding.imgCoin14),
            arrayOf(binding.imgCoin20, binding.imgCoin21, binding.imgCoin22, binding.imgCoin23, binding.imgCoin24),
            arrayOf(binding.imgCoin30, binding.imgCoin31, binding.imgCoin32, binding.imgCoin33, binding.imgCoin34),
            arrayOf(binding.imgCoin40, binding.imgCoin41, binding.imgCoin42, binding.imgCoin43, binding.imgCoin44)
        )

        for (row in 0..4) {
            for (col in 0..4) {
                when (obstaclesMatrix[row][col]) {
                    0 -> viewsMatrix[row][col].visibility = View.INVISIBLE
                    1 -> {
                        viewsMatrix[row][col].setImageResource(R.drawable.img_rock)
                        viewsMatrix[row][col].visibility = View.VISIBLE
                    }
                    2 -> {
                        viewsMatrix[row][col].setImageResource(R.drawable.img_coin)
                        viewsMatrix[row][col].visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateHeartsUI() {
        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE
    }

    private fun updateCountersUI() {
        binding.coinCounter.text = "Coins: $coins"
        binding.distanceCounter.text = "Distance: $distance"
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}