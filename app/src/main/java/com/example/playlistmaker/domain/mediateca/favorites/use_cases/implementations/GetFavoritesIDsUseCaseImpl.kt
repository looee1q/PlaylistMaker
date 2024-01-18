package com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.GetFavoritesIDsUseCase
import kotlinx.coroutines.flow.Flow

class GetFavoritesIDsUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : GetFavoritesIDsUseCase {

    override fun execute(): Flow<List<Long>> {
        return favoriteTracksRepository.showFavoriteTracksIDs()
    }
}