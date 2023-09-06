package com.example.playlistmaker.domain

class PreparePlayerUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute() {
        trackPlayerRepository.preparePlayer()
    }
}