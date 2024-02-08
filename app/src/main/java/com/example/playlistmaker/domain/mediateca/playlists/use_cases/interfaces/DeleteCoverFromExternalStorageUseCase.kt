package com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces

interface DeleteCoverFromExternalStorageUseCase {

    suspend fun execute(coverUri: String)
}