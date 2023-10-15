package com.example.playlistmaker.ui.settings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.ui.settings.view_model.settings.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.sharing.SharingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModel()

    private val sharingViewModel: SharingViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToMainScreenFromSettings.setOnClickListener {
            finish()
        }

        settingsViewModel.liveData.observe(this) {
            binding.nightModeSwitch.isChecked = when (it) {
                is Theme.DarkTheme -> true
                is Theme.DayTheme -> false
            }
        }

        settingsViewModel.getLastSavedTheme()

        binding.nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            val theme = if (isChecked) Theme.DarkTheme() else Theme.DayTheme()
            settingsViewModel.saveAndSetNewTheme(theme)

        }

        binding.shareAppIcon.setOnClickListener {
            sharingViewModel.shareApp()
        }

        binding.writeToTheSupportIcon.setOnClickListener {
            sharingViewModel.writeToSupport()
        }

        binding.userAgreementIcon.setOnClickListener {
            sharingViewModel.getUserAgreement()
        }

    }
}