package com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations

import com.example.playlistmaker.domain.search.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.use_cases.interfaces.WriteHistoryTrackListToStorageUseCase

class WriteHistoryTrackListToStorageUseCaseImpl(private val historyTrackListDAO: HistoryTrackListDAO) :
    WriteHistoryTrackListToStorageUseCase {
    override fun execute(trackList: List<Track>) {
        historyTrackListDAO.saveHistoryTrackList(trackList)
    }
}