package com.example.playlistmaker.data.settings

import android.content.Context
import com.example.playlistmaker.domain.settings.model.Theme

class SharedPrefThemeStorage(private val context: Context) : ThemeStorage {

    private val sharedReferences = context.getSharedPreferences(NIGHT_MODE_SWITCH, Context.MODE_PRIVATE)

    override fun saveTheme(theme : Theme) {

        val isDarkThemeActive = when (theme) {
            is Theme.DarkTheme -> true
            is Theme.DayTheme -> false
        }

        sharedReferences.edit()
            .putBoolean(NIGHT_MODE_STATUS, isDarkThemeActive)
            .apply()
    }

    override fun getSavedTheme(): Theme {
        val isDarkThemeActive = sharedReferences.getBoolean(
            NIGHT_MODE_STATUS,
            SystemNightModeStatus.getSystemNightModeStatus(context)
        )
        return if (isDarkThemeActive) Theme.DarkTheme() else Theme.DayTheme()
    }

    companion object {
        const val NIGHT_MODE_SWITCH = "NIGHT_MODE_SWITCH"
        const val NIGHT_MODE_STATUS = "NIGHT_MODE_STATUS"
    }
}