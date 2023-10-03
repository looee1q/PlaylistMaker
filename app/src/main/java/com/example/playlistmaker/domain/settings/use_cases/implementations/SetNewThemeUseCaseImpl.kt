package com.example.playlistmaker.domain.settings.use_cases.implementations

import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.settings.repository.ThemeRepository
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SetNewThemeUseCase

class SetNewThemeUseCaseImpl(private val themeRepository: ThemeRepository) : SetNewThemeUseCase {
    override fun execute(theme: Theme) {
        themeRepository.setNewTheme(theme)
    }
}