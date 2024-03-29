package com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.AddTrackToFavoritesUseCase
import com.example.playlistmaker.domain.model.Track

class AddTrackToFavoritesUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : AddTrackToFavoritesUseCase {

    override suspend fun execute(track: Track) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }
}