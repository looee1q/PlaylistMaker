package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.GetPlayerStateUseCase

class GetPlayerStateUseCaseImpl(private val playerRepository: PlayerRepository) :
    GetPlayerStateUseCase {
    override fun execute() : PlayerState {
        return playerRepository.getPlayerState()
    }
}