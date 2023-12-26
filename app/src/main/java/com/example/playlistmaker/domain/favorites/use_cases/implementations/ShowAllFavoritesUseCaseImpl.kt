package com.example.playlistmaker.domain.favorites.use_cases.implementations

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.use_cases.interfaces.ShowAllFavoritesUseCase
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class ShowAllFavoritesUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : ShowAllFavoritesUseCase {

    override fun execute(): Flow<List<Track>> {
        return favoriteTracksRepository.showFavoriteTracks()
    }
}