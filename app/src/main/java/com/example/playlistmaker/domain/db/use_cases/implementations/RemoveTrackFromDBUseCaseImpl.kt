package com.example.playlistmaker.domain.db.use_cases.implementations

import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.use_cases.interfaces.RemoveTrackFromDBUseCase
import com.example.playlistmaker.domain.model.Track

class RemoveTrackFromDBUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : RemoveTrackFromDBUseCase {

    override suspend fun execute(track: Track) {
        favoriteTracksRepository.removeTrackFromDB(track)
    }

}