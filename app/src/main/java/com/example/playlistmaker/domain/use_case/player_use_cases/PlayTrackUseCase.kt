package com.example.playlistmaker.domain.use_case.player_use_cases

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class PlayTrackUseCase(private val playerRepository: PlayerRepository) {
    fun execute(doSmthWhilePlaying: () -> Unit) : PlayerState {
        playerRepository.play()
        doSmthWhilePlaying.invoke()
        return playerRepository.getPlayerState()
    }
}