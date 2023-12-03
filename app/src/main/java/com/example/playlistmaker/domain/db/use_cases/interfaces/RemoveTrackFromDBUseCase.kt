package com.example.playlistmaker.domain.db.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface RemoveTrackFromDBUseCase {

    suspend fun execute(track: Track)
}