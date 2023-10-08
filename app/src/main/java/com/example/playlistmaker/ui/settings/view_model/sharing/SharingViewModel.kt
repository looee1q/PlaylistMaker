package com.example.playlistmaker.ui.settings.view_model.sharing

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.ShareAppUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.WriteToSupportUseCase

class SharingViewModel(
    private val shareAppUseCase: ShareAppUseCase,
    private val writeToSupportUseCase: WriteToSupportUseCase,
    private val openTermsUseCase: OpenTermsUseCase
) : ViewModel() {

    fun shareApp() {
        shareAppUseCase.execute()
    }

    fun writeToSupport() {
        writeToSupportUseCase.execute()
    }

    fun getUserAgreement() {
        openTermsUseCase.execute()
    }
}