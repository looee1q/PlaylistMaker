package com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.DeleteCoverFromExternalStorageUseCase

class DeleteCoverFromExternalStorageUseCaseImpl(
    private val playlistsRepository: PlaylistsRepository
): DeleteCoverFromExternalStorageUseCase {
    override suspend fun execute(coverUri: String) {
        playlistsRepository.deleteCoverFromExternalStorage(coverUri)
    }
}