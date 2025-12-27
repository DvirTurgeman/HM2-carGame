package com.guy.class26a_and_3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guy.class26a_and_3.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.slowModeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("gameMode", "button")
            intent.putExtra("gameSpeed", "slow")
            startActivity(intent)
        }

        binding.fastModeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("gameMode", "button")
            intent.putExtra("gameSpeed", "fast")
            startActivity(intent)
        }

        binding.sensorModeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("gameMode", "sensor")
            startActivity(intent)
        }

    }
}