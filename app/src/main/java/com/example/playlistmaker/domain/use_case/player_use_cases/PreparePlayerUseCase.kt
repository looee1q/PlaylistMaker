package com.example.playlistmaker.domain.use_case.player_use_cases

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class PreparePlayerUseCase(private val playerRepository: PlayerRepository) {
    fun execute() : PlayerState {
        playerRepository.preparePlayer()
        return playerRepository.getPlayerState()
    }
}