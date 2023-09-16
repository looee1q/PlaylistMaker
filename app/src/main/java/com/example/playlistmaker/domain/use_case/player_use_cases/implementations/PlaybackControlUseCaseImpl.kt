package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PlaybackControlUseCase

class PlaybackControlUseCaseImpl(private val playerRepository: PlayerRepository) :
    PlaybackControlUseCase {
    override fun execute(doSmthWhilePlaying : () -> Unit, doSmthWhileOnPause : () -> Unit) : PlayerState {
        val playerState = playerRepository.getPlayerState()
        return when (playerState) {
            PlayerState.PLAYING -> PauseTrackUseCaseImpl(playerRepository).execute(doSmthWhileOnPause)
            PlayerState.PAUSED, PlayerState.PREPARED -> PlayTrackUseCaseImpl(playerRepository).execute(doSmthWhilePlaying)
            PlayerState.DEFAULT -> PlayerState.DEFAULT
        }
    }
}