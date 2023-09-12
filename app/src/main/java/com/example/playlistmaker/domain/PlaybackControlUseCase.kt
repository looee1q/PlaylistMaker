package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto

class PlaybackControlUseCase(val trackPlayerRepository: TrackPlayerRepository) {
    fun execute(track: TrackDto) {
        trackPlayerRepository.playbackControl(track)
    }
}