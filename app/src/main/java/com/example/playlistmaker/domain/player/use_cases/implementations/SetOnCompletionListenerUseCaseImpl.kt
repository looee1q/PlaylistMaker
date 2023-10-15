package com.example.playlistmaker.domain.player.use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.use_cases.interfaces.SetOnCompletionListenerUseCase

class SetOnCompletionListenerUseCaseImpl(private val playerRepository: PlayerRepository) :
    SetOnCompletionListenerUseCase {
    override fun execute(setPlayerState: () -> Unit) {
        playerRepository.setOnCompletionListener(setPlayerState)
    }
}