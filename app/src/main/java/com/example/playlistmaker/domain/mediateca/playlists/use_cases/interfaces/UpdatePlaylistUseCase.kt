package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface UpdatePlaylistUseCase {

    suspend fun execute(playlist: Playlist, track: Track)
}