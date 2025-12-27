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

    // מנגנון הטיימר
    private val handler = Handler(Looper.getMainLooper())
    private var delay = 1000L // מהירות המשחק (1000 מילישניות = שנייה אחת)
    private var isGameRunning = false

    // מצב המשחק
    private var carPosition = 2 // 0-4 for 5 lanes, starting in the middle
    private var lives = 3

    // מטריצה לוגית שמייצגת איפה יש אבנים (0=ריק, 1=אבן)
    // 5 rows, 5 columns
    private val obstaclesMatrix = Array(5) { IntArray(5) { 0 } }

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
        // הגדרת כפתורים
        binding.btnLeft.setOnClickListener { moveCar(-1) }
        binding.btnRight.setOnClickListener { moveCar(1) }

        updateCarUI()
        updateHeartsUI()
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (isGameRunning) {
                tick() // ביצוע מהלך אחד במשחק
                handler.postDelayed(this, delay) // תזמון המהלך הבא
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

    // הפונקציה המרכזית: מזיזה הכל למטה ובודקת מה קרה
    private fun tick() {
        // 1. הזזת המכשולים למטה (Shift Down)
        for (row in 4 downTo 1) {
            for (col in 0..4) {
                obstaclesMatrix[row][col] = obstaclesMatrix[row - 1][col]
            }
        }

        // 2. ניקוי שורה עליונה ויצירת אבן חדשה
        for (col in 0..4) {
            obstaclesMatrix[0][col] = 0
        }

        val isObstacle = Random.nextBoolean() // האם ליצור אבן?
        if (isObstacle) {
            val randomLane = Random.nextInt(0, 5) // באיזה נתיב?
            obstaclesMatrix[0][randomLane] = 1
        }

        // 3. עדכון המסך (כדי שנראה את האבנים זזות)
        updateObstaclesUI()

        // 4. בדיקת התנגשות אחרי שהאבנים ירדו
        checkCollision()
    }

    private fun checkCollision() {
        // אם במיקום הנוכחי של הרכב (בשורה התחתונה מס\' 4) יש אבן (1)
        if (obstaclesMatrix[4][carPosition] == 1) {
            crash()
        }
    }

    private fun crash() {
        lives--
        updateHeartsUI()

        Toast.makeText(this, "CRASH!", Toast.LENGTH_SHORT).show()
        vibrate() // רטט

        // המכשול "מתנפץ" אז נוריד אותו מהמטריצה כדי שלא נתנגש בו שוב ושוב
        obstaclesMatrix[4][carPosition] = 0
        updateObstaclesUI()

        if (lives == 0) {
            Toast.makeText(this, "GAME OVER! Restarting...", Toast.LENGTH_LONG).show()
            // איפוס מלא של המשחק
            lives = 3
            updateHeartsUI()
            // ניקוי המטריצה
            for (i in 0..4) {
                for (j in 0..4) {
                    obstaclesMatrix[i][j] = 0
                }
            }
            updateObstaclesUI()
        }
    }

    private fun moveCar(direction: Int) {
        // שינוי מיקום
        carPosition += direction

        // הגבלת גבולות (שלא נצא מהמסך)
        if (carPosition < 0) carPosition = 0
        if (carPosition > 4) carPosition = 4

        updateCarUI()

        // בדיקת התנגשות גם כשאנחנו זזים לתוך אבן קיימת
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
        // רשימה של ה-Views כדי שנוכל לגשת אליהם בלולאה
        val viewsMatrix = arrayOf(
            arrayOf(binding.imgCoin00, binding.imgCoin01, binding.imgCoin02, binding.imgCoin03, binding.imgCoin04),
            arrayOf(binding.imgCoin10, binding.imgCoin11, binding.imgCoin12, binding.imgCoin13, binding.imgCoin14),
            arrayOf(binding.imgCoin20, binding.imgCoin21, binding.imgCoin22, binding.imgCoin23, binding.imgCoin24),
            arrayOf(binding.imgCoin30, binding.imgCoin31, binding.imgCoin32, binding.imgCoin33, binding.imgCoin34),
            arrayOf(binding.imgCoin40, binding.imgCoin41, binding.imgCoin42, binding.imgCoin43, binding.imgCoin44)
        )

        for (row in 0..4) {
            for (col in 0..4) {
                if (obstaclesMatrix[row][col] == 1) {
                    viewsMatrix[row][col].visibility = View.VISIBLE
                } else {
                    viewsMatrix[row][col].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun updateHeartsUI() {
        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE
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