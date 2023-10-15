package com.example.playlistmaker.data.settings

import android.content.Context
import android.content.res.Configuration
import android.util.Log

object SystemNightModeStatus {

    fun getSystemNightModeStatus(context: Context) : Boolean {
        return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
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