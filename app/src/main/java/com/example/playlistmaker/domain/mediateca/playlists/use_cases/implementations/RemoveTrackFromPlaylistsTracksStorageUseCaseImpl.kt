package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.RemoveTrackFromPlaylistsTracksStorageUseCase
import com.example.playlistmaker.domain.model.Track

class RemoveTrackFromPlaylistsTracksStorageUseCaseImpl(
    private val playlistsRepository: PlaylistsRepository
) : RemoveTrackFromPlaylistsTracksStorageUseCase {

    override suspend fun execute(track: Track) {
        playlistsRepository.removeTrackFromPlaylistsTracksStorage(track)
    }
}