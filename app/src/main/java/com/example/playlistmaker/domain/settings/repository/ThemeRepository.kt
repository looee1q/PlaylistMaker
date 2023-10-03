package com.example.playlistmaker.domain.settings.repository

import com.example.playlistmaker.domain.settings.model.Theme

interface ThemeRepository {
    fun saveTheme(theme: Theme)

    fun getLastSavedTheme() : Theme

    fun setNewTheme(theme: Theme)
}