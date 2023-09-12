package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto

interface TrackPlayerRepository {
    fun playTrack(track: TrackDto)
    fun pauseTrack(track: TrackDto)
    fun playbackControl(track: TrackDto)
    fun preparePlayer()
    fun destroy()
    fun updateTrackTime(track: TrackDto)
}