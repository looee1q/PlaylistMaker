package com.example.playlistmaker.ui.settings.view_model.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory : ViewModelProvider.Factory {

    private val saveThemeUseCase by lazy { Creator.provideSaveThemeUseCase() }
    private val getLastSavedThemeUseCase by lazy { Creator.provideGetLastSavedThemeUseCase() }
    private val setNewThemeUseCase by lazy { Creator.provideSetNewThemeUseCase() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            saveThemeUseCase = saveThemeUseCase,
            getLastSavedThemeUseCase = getLastSavedThemeUseCase,
            setNewThemeUseCase = setNewThemeUseCase
        ) as T
    }
}