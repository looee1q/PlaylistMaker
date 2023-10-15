package com.example.playlistmaker.domain.player.use_cases.implementations

import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayingTrackTimeUseCase

class GetPlayingTrackTimeUseCaseImpl(private val playerRepository: PlayerRepository) :
    GetPlayingTrackTimeUseCase {
    override fun execute() : Int {
        return playerRepository.getCurrentTime()
    }
}