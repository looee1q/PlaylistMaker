package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.GetPlaylistByIdUseCase

class GetPlaylistByIdUseCaseImpl(private val playlistsRepository: PlaylistsRepository): GetPlaylistByIdUseCase {
    override suspend fun execute(playlistId: Int): Playlist {
        return playlistsRepository.getPlaylist(playlistId)
    }
}