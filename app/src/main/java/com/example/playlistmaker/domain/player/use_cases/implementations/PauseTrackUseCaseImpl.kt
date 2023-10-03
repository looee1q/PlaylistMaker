package com.example.playlistmaker.domain.player.use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.use_cases.interfaces.PauseTrackUseCase

class PauseTrackUseCaseImpl(private val playerRepository: PlayerRepository) : PauseTrackUseCase {
    override fun execute(doActionWhileOnPause: () -> Unit) : PlayerState {
        playerRepository.pause()
        doActionWhileOnPause.invoke()
        return playerRepository.getPlayerState()
    }
}