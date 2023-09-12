package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto

class PauseTrackUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: TrackDto) {
        trackPlayerRepository.pauseTrack(track = track)
    }
}