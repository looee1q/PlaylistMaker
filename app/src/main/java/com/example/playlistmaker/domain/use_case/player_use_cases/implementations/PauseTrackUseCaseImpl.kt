package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PauseTrackUseCase

class PauseTrackUseCaseImpl(private val playerRepository: PlayerRepository) : PauseTrackUseCase {
    override fun execute(doSmthWhileOnPause: () -> Unit) : PlayerState {
        playerRepository.pause()
        doSmthWhileOnPause.invoke()
        return playerRepository.getPlayerState()
    }
}