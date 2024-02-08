package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.GetAllTracksFromPlaylistUseCase
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class GetAllTracksFromPlaylistUseCaseImpl(
    private val playlistsRepository: PlaylistsRepository
) : GetAllTracksFromPlaylistUseCase {
    override fun execute(tracksId: List<Long>): Flow<List<Track>> {
        return playlistsRepository.getAllTracksFromPlaylist(tracksId)
    }
}