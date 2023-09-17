package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PlayTrackUseCase

class PlayTrackUseCaseImpl(private val playerRepository: PlayerRepository) : PlayTrackUseCase {
    override fun execute(doActionWhilePlaying: () -> Unit) : PlayerState {
        playerRepository.play()
        doActionWhilePlaying.invoke()
        return playerRepository.getPlayerState()
    }
}