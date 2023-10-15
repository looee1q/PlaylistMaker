package com.example.playlistmaker.domain.sharing.use_cases.implementations

import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.ShareAppUseCase

class ShareAppUseCaseImpl(private val externalNavigator: ExternalNavigator) : ShareAppUseCase {
    override fun execute() {
        externalNavigator.shareLink(externalNavigator.getShareAppLink())
    }
}