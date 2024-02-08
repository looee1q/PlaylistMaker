package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface GetAllTracksFromPlaylistUseCase {

    fun execute(tracksId: List<Long>): Flow<List<Track>>
}