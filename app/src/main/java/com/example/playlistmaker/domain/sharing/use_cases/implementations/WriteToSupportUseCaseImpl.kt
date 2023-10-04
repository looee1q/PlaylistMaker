package com.example.playlistmaker.domain.sharing.use_cases.implementations

import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.WriteToSupportUseCase

class WriteToSupportUseCaseImpl(private val externalNavigator: ExternalNavigator) : WriteToSupportUseCase {
    override fun execute() {
        externalNavigator.sendEmail(externalNavigator.getSupportEmailData())
    }
}