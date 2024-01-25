package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface AddTrackToPlaylistUseCase {

   suspend fun execute(track: Track, playlist: Playlist)
}