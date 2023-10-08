package com.example.playlistmaker.ui.settings.view_model.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.settings.use_cases.interfaces.GetLastSavedThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SaveThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SetNewThemeUseCase

class SettingsViewModel(
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getLastSavedThemeUseCase: GetLastSavedThemeUseCase,
    private val setNewThemeUseCase: SetNewThemeUseCase
) : ViewModel() {

    private val mutableLiveData = MutableLiveData<Theme>()
    val liveData: LiveData<Theme> = mutableLiveData

    fun saveAndSetNewTheme(theme: Theme) {
        mutableLiveData.value = theme
        saveThemeUseCase.execute(theme)
        setNewThemeUseCase.execute(theme)
    }

    fun getLastSavedTheme() {
        mutableLiveData.value = getLastSavedThemeUseCase.execute()
    }

}