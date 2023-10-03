package com.example.playlistmaker.domain.use_case.player_use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.DestroyPlayerUseCase

class DestroyPlayerUseCaseImpl(private val playerRepository: PlayerRepository) :
    DestroyPlayerUseCase {
    override fun execute() {
        playerRepository.destroyPlayer()
    }
}