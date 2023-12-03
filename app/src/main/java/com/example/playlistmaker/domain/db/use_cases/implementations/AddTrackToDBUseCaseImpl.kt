package com.example.playlistmaker.domain.db.use_cases.implementations

import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.use_cases.interfaces.AddTrackToDBUseCase
import com.example.playlistmaker.domain.model.Track

class AddTrackToDBUseCaseImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : AddTrackToDBUseCase {

    override suspend fun execute(track: Track) {
        favoriteTracksRepository.addTrackToDB(track)
    }
}