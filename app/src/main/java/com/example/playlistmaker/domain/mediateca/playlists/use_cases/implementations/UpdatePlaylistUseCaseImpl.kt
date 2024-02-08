package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.UpdatePlaylistUseCase
import com.example.playlistmaker.domain.model.Track

class UpdatePlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : UpdatePlaylistUseCase {
    override suspend fun execute(playlist: Playlist, track: Track) {
        playlistsRepository.updatePlaylist(playlist, track)
    }

    override suspend fun execute(playlistWithUpdatedData: Playlist) {
        playlistsRepository.updatePlaylist(playlistWithUpdatedData)
    }
}