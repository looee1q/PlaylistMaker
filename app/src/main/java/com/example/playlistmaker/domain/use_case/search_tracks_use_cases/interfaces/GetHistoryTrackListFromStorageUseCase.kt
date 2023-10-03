package com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface GetHistoryTrackListFromStorageUseCase {
    fun execute(): List<Track>
}