package com.example.playlistmaker.data.search.dao

import com.example.playlistmaker.data.search.mapper.Mapper
import com.example.playlistmaker.domain.search.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track

class HistoryTrackListDAOImpl(private val trackListStorage: TrackListStorage): HistoryTrackListDAO {
    override fun getHistoryTrackList(): List<Track> {
        val savedTrackList = trackListStorage.getTrackList()
        return savedTrackList.map { Mapper.mapSimpeTrackToTrack(it) }
    }

    override fun saveHistoryTrackList(trackList: List<Track>) {
        val dataTrackList = trackList.map { Mapper.mapTrackToSimpleTrack(it) }
        trackListStorage.saveTrackList(dataTrackList)
    }
}