package com.example.playlistmaker.domain.favorites.use_cases.implementations

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.use_cases.interfaces.RemoveTrackFromFavoritesUseCase
import com.example.playlistmaker.domain.model.Track

class RemoveTrackFromFavoritesUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : RemoveTrackFromFavoritesUseCase {

    override suspend fun execute(track: Track) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }

}