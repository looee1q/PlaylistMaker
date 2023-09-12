package com.example.playlistmaker.data.dao

import com.example.playlistmaker.data.dto.SimpleTrack

interface TrackListStorage {
    fun saveTrackList(trackList: List<SimpleTrack>)

    fun getTrackList(): List<SimpleTrack>
}