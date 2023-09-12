package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto

class UpdateTrackTimeUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: TrackDto) {
        trackPlayerRepository.updateTrackTime(track)
    }
}