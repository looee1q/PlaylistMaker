package com.example.playlistmaker.domain.db.use_cases.implementations

import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.use_cases.interfaces.ShowAllTracksFromDBUseCase
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class ShowAllTracksFromDBUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : ShowAllTracksFromDBUseCase {

    override fun execute(): Flow<List<Track>> {
        return favoriteTracksRepository.showFavoriteTracks()
    }
}