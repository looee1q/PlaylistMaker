package com.example.playlistmaker.domain.player.use_cases.implementations

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.use_cases.interfaces.PreparePlayerUseCase

class PreparePlayerUseCaseImpl(private val playerRepository: PlayerRepository) :
    PreparePlayerUseCase {
    override fun execute(track: Track, setPlayerState: () -> Unit) {
        playerRepository.preparePlayer(track, setPlayerState)
    }
}