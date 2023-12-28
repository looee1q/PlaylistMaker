package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface ShowPlaylistsUseCase {
    fun execute(): Flow<List<Playlist>>
}