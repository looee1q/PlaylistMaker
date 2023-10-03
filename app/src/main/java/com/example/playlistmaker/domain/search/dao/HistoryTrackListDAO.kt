package com.example.playlistmaker.domain.search.dao

import com.example.playlistmaker.domain.model.Track

interface HistoryTrackListDAO {
    fun getHistoryTrackList(): List<Track>
    fun saveHistoryTrackList(trackList: List<Track>)
}