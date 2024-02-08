package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

interface DeletePlaylistUseCase {

    suspend fun execute(playlist: Playlist)
}