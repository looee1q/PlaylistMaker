package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface AddTrackToPlaylistsTracksStorageUseCase {

   suspend fun execute(track: Track)
}