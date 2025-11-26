package com.guy.class26a_and_3

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guy.class26a_and_3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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


        val map = arrayOf(
            arrayOf(
                binding.imgCoin00,
                binding.imgCoin01,
                binding.imgCoin02
            ),
            arrayOf(
                binding.imgCoin10,
                binding.imgCoin01,
                binding.imgCoin11
            ),
            arrayOf(
                binding.imgCoin20,
                binding.imgCoin21,
                binding.imgCoin21
            ),

        )

        map[0][1].visibility = View.VISIBLE
    }
}