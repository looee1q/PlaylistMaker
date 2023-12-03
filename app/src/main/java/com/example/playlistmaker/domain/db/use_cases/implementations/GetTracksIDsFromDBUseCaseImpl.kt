package com.example.playlistmaker.domain.db.use_cases.implementations

import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.use_cases.interfaces.GetTracksIDsFromDBUseCase
import kotlinx.coroutines.flow.Flow

class GetTracksIDsFromDBUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : GetTracksIDsFromDBUseCase {

    override fun execute(): Flow<List<Long>> {
        return favoriteTracksRepository.showFavoriteTracksIDs()
    }
}