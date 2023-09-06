package com.example.playlistmaker.domain

class DestroyMediaPlayerUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute() {
        trackPlayerRepository.destroy()
    }
}