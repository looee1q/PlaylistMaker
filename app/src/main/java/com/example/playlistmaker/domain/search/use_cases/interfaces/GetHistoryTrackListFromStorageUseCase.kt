package com.example.playlistmaker.domain.search.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface GetHistoryTrackListFromStorageUseCase {
    fun execute(): List<Track>
}