package com.example.playlistmaker.ui.settings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val saveThemeUseCase by lazy { Creator.provideSaveThemeUseCase() }
    private val getLastSavedThemeUseCase by lazy { Creator.provideGetLastSavedThemeUseCase() }
    private val setNewThemeUseCase by lazy { Creator.provideSetNewThemeUseCase() }

    private val shareAppUseCase by lazy { Creator.provideShareAppUseCase() }
    private val writeToSupportUseCase by lazy { Creator.provideWriteToSupportUseCase() }
    private val openTermsUseCase by lazy { Creator.provideOpenTermsUseCase() }
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
            shareAppUseCase.execute()
        }

        binding.writeToTheSupportIcon.setOnClickListener {
            writeToSupportUseCase.execute()
        }

        binding.userAgreementIcon.setOnClickListener {
            openTermsUseCase.execute()
        }

    }
}