package com.example.playlistmaker.domain.settings.use_cases.interfaces

import com.example.playlistmaker.domain.settings.model.Theme

interface SetNewThemeUseCase {
    fun execute(theme: Theme)
}