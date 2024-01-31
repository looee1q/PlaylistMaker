package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

interface SaveCoverToExternalStorageUseCase {

    suspend fun execute(coverUri: String): String
}