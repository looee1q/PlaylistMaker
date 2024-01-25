package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.model.Track

class AddTrackToPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : AddTrackToPlaylistUseCase {
    override suspend fun execute(track: Track, playlist: Playlist) {
        playlistsRepository.addTrackToPlaylist(track, playlist)
    }
}