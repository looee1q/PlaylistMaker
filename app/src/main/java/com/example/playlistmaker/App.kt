package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val NIGHT_MODE_SWITCH = "NIGHT_MODE_SWITCH"
const val NIGHT_MODE_STATUS = "NIGHT_MODE_STATUS"

class App: Application() {

    var darkTheme = false
    lateinit var sharedReferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedReferences = getSharedPreferences(NIGHT_MODE_SWITCH, MODE_PRIVATE)
        darkTheme = sharedReferences.getBoolean(NIGHT_MODE_STATUS, false)
        changeTheme(darkTheme)
     }

    fun changeTheme(darkThemeEnabled: Boolean) {
        var nightModeState = AppCompatDelegate.MODE_NIGHT_NO
        if (darkThemeEnabled) {
            nightModeState = AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightModeState)

        darkTheme = darkThemeEnabled
        sharedReferences.edit()
            .putBoolean(NIGHT_MODE_STATUS, darkTheme)
            .apply()
    }

}