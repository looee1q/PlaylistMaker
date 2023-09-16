package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PreparePlayerUseCase

class PreparePlayerUseCaseImpl(private val playerRepository: PlayerRepository) :
    PreparePlayerUseCase {
    override fun execute() : PlayerState {
        playerRepository.preparePlayer()
        return playerRepository.getPlayerState()
    }
}