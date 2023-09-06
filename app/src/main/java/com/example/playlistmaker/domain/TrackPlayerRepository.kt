package com.example.playlistmaker.domain

import com.example.playlistmaker.Track

interface TrackPlayerRepository {
    fun playTrack(track: Track)
    fun pauseTrack(track: Track)
    fun playbackControl(track: Track)
    fun preparePlayer()
    fun destroy()
    fun updateTrackTime(track: Track)
}