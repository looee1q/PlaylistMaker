package com.example.playlistmaker.domain.settings.use_cases.implementations

import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.settings.repository.ThemeRepository
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SaveThemeUseCase

class SaveThemeUseCaseImpl(private val changeThemeRepository: ThemeRepository) : SaveThemeUseCase {
    override fun execute(theme: Theme) {
        changeThemeRepository.saveTheme(theme)
    }
}