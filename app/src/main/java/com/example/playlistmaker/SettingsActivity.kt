package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backToMainScreen = findViewById<ImageView>(R.id.to_main_screen_from_settings)
        backToMainScreen.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(displayIntent)
        }
    }
}