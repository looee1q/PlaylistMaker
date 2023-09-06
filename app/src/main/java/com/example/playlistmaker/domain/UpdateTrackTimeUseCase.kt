package com.example.playlistmaker.domain

import com.example.playlistmaker.Track

class UpdateTrackTimeUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: Track) {
        trackPlayerRepository.updateTrackTime(track)
    }
}