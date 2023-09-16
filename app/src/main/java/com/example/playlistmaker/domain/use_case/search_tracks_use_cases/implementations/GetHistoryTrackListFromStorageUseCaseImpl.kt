package com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations

import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces.GetHistoryTrackListFromStorageUseCase

class GetHistoryTrackListFromStorageUseCaseImpl(private val historyTrackListDAO: HistoryTrackListDAO) :
    GetHistoryTrackListFromStorageUseCase {
    override fun execute(): List<Track> {
        return historyTrackListDAO.getHistoryTrackList()
    }
}