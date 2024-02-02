package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import android.util.Log
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddPlaylistUseCase

class AddPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : AddPlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        Log.d("AddPlaylistUseCaseImpl", "Cейчас буду создавать новый плейлист")
        playlistsRepository.createPlaylist(playlist)
        Log.d("AddPlaylistUseCaseImpl", "Я должен был создать плейлист")
    }
}