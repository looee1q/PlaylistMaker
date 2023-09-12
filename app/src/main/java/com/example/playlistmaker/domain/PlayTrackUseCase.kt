package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto

class PlayTrackUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: TrackDto) {
        trackPlayerRepository.pauseTrack(track = track)
    }
}