package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backToMainScreen = findViewById<ImageView>(R.id.to_main_screen_from_settings)
        val switch = findViewById<SwitchCompat>(R.id.night_mode_switch)
        val shareAppIcon = findViewById<ImageView>(R.id.share_app_icon)
        val writeToTheSupportIcon = findViewById<ImageView>(R.id.write_to_the_support_icon)
        val userAgreementIcon = findViewById<ImageView>(R.id.user_agreement_icon)

        backToMainScreen.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(displayIntent)
        }

        switch.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        shareAppIcon.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_user_agreement))
            }
            val chooser: Intent = Intent.createChooser(shareIntent, "")
            startActivity(chooser)
        }

        writeToTheSupportIcon.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_default_recipient)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_default_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_default_text))
            }
            startActivity(sendIntent)
        }

        userAgreementIcon.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.link_to_offer))
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
            startActivity(sendIntent)
        }

    }
}