package com.example.playlistmaker.domain.settings.use_cases.implementations

import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.settings.repository.ThemeRepository
import com.example.playlistmaker.domain.settings.use_cases.interfaces.GetLastSavedThemeUseCase

class GetLastSavedThemeUseCaseImpl(private val themeRepository: ThemeRepository) : GetLastSavedThemeUseCase {
    override fun execute(): Theme {
        return themeRepository.getLastSavedTheme()
    }
}