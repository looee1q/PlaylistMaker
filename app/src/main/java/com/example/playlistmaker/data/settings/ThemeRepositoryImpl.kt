package com.example.playlistmaker.data.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.settings.repository.ThemeRepository

class ThemeRepositoryImpl(private val storage: ThemeStorage) : ThemeRepository {

    override fun saveTheme(theme: Theme) {
        storage.saveTheme(theme)
    }

    override fun getLastSavedTheme(): Theme {
        return storage.getSavedTheme()
    }

    override fun setNewTheme(theme: Theme) {
        val nightModeState = when (theme) {
            is Theme.DarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
            is Theme.DayTheme -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightModeState)
    }
}