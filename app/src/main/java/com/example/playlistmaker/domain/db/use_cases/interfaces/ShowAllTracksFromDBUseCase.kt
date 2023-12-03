package com.example.playlistmaker.domain.db.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface ShowAllTracksFromDBUseCase {

    fun execute(): Flow<List<Track>>
}