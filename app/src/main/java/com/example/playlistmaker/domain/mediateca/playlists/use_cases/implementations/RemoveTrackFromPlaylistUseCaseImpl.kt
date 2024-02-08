package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.RemoveTrackFromPlaylistUseCase
import com.example.playlistmaker.domain.model.Track

class RemoveTrackFromPlaylistUseCaseImpl(
    private val playlistsRepository: PlaylistsRepository
) : RemoveTrackFromPlaylistUseCase {

    override suspend fun execute(track: Track, playlist: Playlist) {
        playlistsRepository.removeTrackFromPlaylist(track, playlist)
    }
}