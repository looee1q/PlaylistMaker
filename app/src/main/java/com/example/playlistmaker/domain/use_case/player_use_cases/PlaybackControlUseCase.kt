package com.example.playlistmaker.domain.use_case.player_use_cases

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class PlaybackControlUseCase(private val playerRepository: PlayerRepository) {
    fun execute(doSmthWhilePlaying : () -> Unit, doSmthWhileOnPause : () -> Unit) : PlayerState {
        val playerState = playerRepository.getPlayerState()
        return when (playerState) {
            PlayerState.PLAYING -> PauseTrackUseCase(playerRepository).execute(doSmthWhileOnPause)
            PlayerState.PAUSED, PlayerState.PREPARED -> PlayTrackUseCase(playerRepository).execute(doSmthWhilePlaying)
            PlayerState.DEFAULT -> PlayerState.DEFAULT
        }
    }
}