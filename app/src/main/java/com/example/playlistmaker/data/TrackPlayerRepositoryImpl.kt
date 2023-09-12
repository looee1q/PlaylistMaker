package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.TrackPlayerRepository

class TrackPlayerRepositoryImpl(val mediaPlayer: Player) : TrackPlayerRepository {

    override fun playTrack(track: TrackDto) {
        mediaPlayer.start()
    }

    override fun pauseTrack(track: TrackDto) {
        mediaPlayer.pause()
    }

    override fun playbackControl(track: TrackDto) {
        mediaPlayer.playbackControl()
    }

    override fun preparePlayer() {
        mediaPlayer.preparePlayer()
    }

    override fun destroy() {
        mediaPlayer.destroy()
    }

    override fun updateTrackTime(track: TrackDto) {
        mediaPlayer.updateTrackTime()
    }
}