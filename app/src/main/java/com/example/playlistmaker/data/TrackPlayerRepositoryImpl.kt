package com.example.playlistmaker.data

import com.example.playlistmaker.Track
import com.example.playlistmaker.domain.TrackPlayerRepository

class TrackPlayerRepositoryImpl(val mediaPlayer: Player) : TrackPlayerRepository {

    override fun playTrack(track: Track) {
        mediaPlayer.start()
    }

    override fun pauseTrack(track: Track) {
        mediaPlayer.pause()
    }

    override fun playbackControl(track: Track) {
        mediaPlayer.playbackControl()
    }

    override fun preparePlayer() {
        mediaPlayer.preparePlayer()
    }

    override fun destroy() {
        mediaPlayer.destroy()
    }

    override fun updateTrackTime(track: Track) {
        mediaPlayer.updateTrackTime()
    }
}