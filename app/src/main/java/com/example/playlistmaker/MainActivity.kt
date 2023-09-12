package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.ui.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        binding.musicLibraryButton.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MusicLibraryActivity::class.java)
            startActivity(displayIntent)
        }

        binding.settingsButton.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}