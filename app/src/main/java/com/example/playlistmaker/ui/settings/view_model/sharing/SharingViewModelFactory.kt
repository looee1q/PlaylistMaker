package com.example.playlistmaker.ui.settings.view_model.sharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SharingViewModelFactory : ViewModelProvider.Factory {

    private val shareAppUseCase by lazy { Creator.provideShareAppUseCase() }
    private val writeToSupportUseCase by lazy { Creator.provideWriteToSupportUseCase() }
    private val openTermsUseCase by lazy { Creator.provideOpenTermsUseCase() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharingViewModel(
            shareAppUseCase = shareAppUseCase,
            writeToSupportUseCase = writeToSupportUseCase,
            openTermsUseCase = openTermsUseCase
        ) as T
    }
}