package com.example.playlistmaker.domain.favorites.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface AddTrackToFavoritesUseCase {

    suspend fun execute(track: Track)
}