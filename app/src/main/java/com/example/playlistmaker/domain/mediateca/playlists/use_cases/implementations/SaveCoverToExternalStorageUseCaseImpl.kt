package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.SaveCoverToExternalStorageUseCase

class SaveCoverToExternalStorageUseCaseImpl(
    private val playlistsRepository: PlaylistsRepository
) : SaveCoverToExternalStorageUseCase {
    override suspend fun execute(coverUri: String): String {
        return playlistsRepository.saveCoverToExternalStorage(coverUri)
    }
}