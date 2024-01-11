package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddTrackToPlaylistsTracksStorageUseCase
import com.example.playlistmaker.domain.model.Track

class AddTrackToPlaylistsTracksStorageUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : AddTrackToPlaylistsTracksStorageUseCase {
    override suspend fun execute(track: Track) {
        playlistsRepository.addTrackToPlaylistsTracksStorage(track)
    }
}