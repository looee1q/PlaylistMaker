package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.ExternalNavigatorRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.SharePlaylistUseCase

class SharePlaylistUseCaseImpl(
    private val externalNavigatorRepository: ExternalNavigatorRepository
): SharePlaylistUseCase {
    override fun execute(playlistMessage: String) {
        externalNavigatorRepository.sendMessage(playlistMessage)
    }
}