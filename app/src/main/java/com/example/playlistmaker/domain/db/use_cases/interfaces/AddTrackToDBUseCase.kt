package com.example.playlistmaker.domain.db.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface AddTrackToDBUseCase {

    suspend fun execute(track: Track)
}