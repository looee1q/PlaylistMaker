package com.example.playlistmaker.domain.sharing.use_cases.implementations

import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.OpenTermsUseCase

class OpenTermsUseCaseImpl(private val externalNavigator: ExternalNavigator) : OpenTermsUseCase {
    override fun execute() {
        externalNavigator.openLink(externalNavigator.getTermsLink())
    }
}