package com.example.playlistmaker.data.settings

import com.example.playlistmaker.domain.settings.model.Theme

interface ThemeStorage {

    fun saveTheme(theme: Theme)

    fun getSavedTheme(): Theme
}