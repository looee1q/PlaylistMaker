package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

const val NIGHT_MODE_SWI = "NIGHT_MODE_SWI"
const val NIGHT_MODE_STATUS = "NIGHT_MODE_STATUS"

class App: Application() {

    var darkTheme = false
    lateinit var sharedReferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedReferences = getSharedPreferences(NIGHT_MODE_SWI, MODE_PRIVATE)
        darkTheme = sharedReferences.getBoolean(NIGHT_MODE_STATUS, getSystemNightModeStatus())
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

    private fun getSystemNightModeStatus() : Boolean {
        return when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Log.d("NightMode", "UI_MODE_NIGHT_YES")
                true
            }
            else -> {
                Log.d("NightMode", "else")
                false
            }
        }
    }

}