package com.example.playlistmaker.domain.favorites.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface ShowAllFavoritesUseCase {

    fun execute(): Flow<List<Track>>
}