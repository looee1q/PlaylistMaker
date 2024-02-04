package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddPlaylistUseCase

class AddPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : AddPlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }
}