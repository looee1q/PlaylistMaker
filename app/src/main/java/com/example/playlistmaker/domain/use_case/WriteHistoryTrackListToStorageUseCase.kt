package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track

class WriteHistoryTrackListToStorageUseCase(private val historyTrackListDAO: HistoryTrackListDAO) {
    fun execute(trackList: List<Track>) {
        historyTrackListDAO.saveHistoryTrackList(trackList)
    }
}