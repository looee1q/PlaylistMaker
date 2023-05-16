package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediatecaButton = findViewById<Button>(R.id.mediateca_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)
        val searchButtonClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Toast.makeText(this@MainActivity, "Вы нажали на кнопку \"Поиск\"", Toast.LENGTH_SHORT).show()
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)
        mediatecaButton.setOnClickListener {
            //Toast.makeText(this, "Вы нажали на кнопку \"Медиатека\"", Toast.LENGTH_SHORT).show()
            val displayIntent = Intent(this@MainActivity, MediatecaActivity::class.java)
            startActivity(displayIntent)
        }
        settingsButton.setOnClickListener {
            //Toast.makeText(this, "Вы нажали на кнопку \"Настройки\"", Toast.LENGTH_SHORT).show()
            val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}