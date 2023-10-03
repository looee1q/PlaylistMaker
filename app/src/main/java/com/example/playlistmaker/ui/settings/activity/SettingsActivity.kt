package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val saveThemeUseCase by lazy { Creator.provideSaveThemeUseCase() }
    private val getLastSavedThemeUseCase by lazy { Creator.provideGetLastSavedThemeUseCase() }
    private val setNewThemeUseCase by lazy { Creator.provideSetNewThemeUseCase() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToMainScreenFromSettings.setOnClickListener {
            finish()
        }

        binding.nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            val theme = if (isChecked) Theme.DarkTheme() else Theme.DayTheme()
            setNewThemeUseCase.execute(theme)
            saveThemeUseCase.execute(theme)

            switcher.isChecked = isChecked
        }

        binding.nightModeSwitch.isChecked = when (getLastSavedThemeUseCase.execute()) {
            is Theme.DarkTheme -> true
            is Theme.DayTheme -> false
        }

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