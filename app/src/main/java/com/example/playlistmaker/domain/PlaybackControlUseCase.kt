package com.example.playlistmaker.domain

import com.example.playlistmaker.Track

class PlaybackControlUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: Track) {
        trackPlayerRepository.playbackControl(track)
    }
}