package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToMainScreenFromSettings.setOnClickListener {
            finish()
        }

        binding.nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).changeTheme(isChecked)
            switcher.isChecked = isChecked
        }

        binding.nightModeSwitch.isChecked = (applicationContext as App).darkTheme

        binding.shareAppIcon.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_user_agreement))
            }
            val chooser: Intent = Intent.createChooser(shareIntent, "")
            startActivity(chooser)
        }

        binding.writeToTheSupportIcon.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_default_recipient)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_default_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_default_text))
            }
            startActivity(sendIntent)
        }

        binding.userAgreementIcon.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.link_to_offer))
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
            startActivity(sendIntent)
        }

    }
}