package com.example.playlistmaker.domain.use_case.player_use_cases

import com.example.playlistmaker.domain.player.PlayerRepository

class DestroyPlayerUseCase(private val playerRepository: PlayerRepository) {
    fun execute() {
        playerRepository.destroyPlayer()
    }
}