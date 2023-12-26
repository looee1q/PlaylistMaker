package com.example.playlistmaker.domain.favorites.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface RemoveTrackFromFavoritesUseCase {

    suspend fun execute(track: Track)
}