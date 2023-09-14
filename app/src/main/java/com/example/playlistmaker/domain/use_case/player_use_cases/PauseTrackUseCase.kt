package com.example.playlistmaker.domain.use_case.player_use_cases

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class PauseTrackUseCase(private val playerRepository: PlayerRepository) {
    fun execute(doSmthWhileOnPause: () -> Unit) : PlayerState {
        playerRepository.pause()
        doSmthWhileOnPause.invoke()
        return playerRepository.getPlayerState()
    }
}