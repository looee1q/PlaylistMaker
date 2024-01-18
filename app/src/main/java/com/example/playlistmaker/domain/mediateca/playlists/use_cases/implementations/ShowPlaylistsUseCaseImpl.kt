package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.ShowPlaylistsUseCase
import kotlinx.coroutines.flow.Flow

class ShowPlaylistsUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : ShowPlaylistsUseCase {
    override fun execute(): Flow<List<Playlist>> {
        return playlistsRepository.showPlaylists()
    }
}