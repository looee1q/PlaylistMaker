package com.example.playlistmaker.data.search.dao

import com.example.playlistmaker.data.search.dto.SimpleTrack

interface TrackListStorage {
    fun saveTrackList(trackList: List<SimpleTrack>)

    fun getTrackList(): List<SimpleTrack>
}