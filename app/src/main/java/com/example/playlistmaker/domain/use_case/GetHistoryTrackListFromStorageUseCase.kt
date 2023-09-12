package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track

class GetHistoryTrackListFromStorageUseCase(private val historyTrackListDAO: HistoryTrackListDAO) {
    fun execute(): List<Track> {
        return historyTrackListDAO.getHistoryTrackList()
    }
}