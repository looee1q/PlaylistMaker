package com.example.playlistmaker.domain

import com.example.playlistmaker.Track

class PlayTrackUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: Track) {
        trackPlayerRepository.pauseTrack(track = track)
    }
}