package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.DeletePlaylistUseCase

class DeletePlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : DeletePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }
}