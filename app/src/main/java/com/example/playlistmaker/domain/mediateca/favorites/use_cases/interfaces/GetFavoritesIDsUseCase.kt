package com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces

import kotlinx.coroutines.flow.Flow

interface GetFavoritesIDsUseCase {

    fun execute(): Flow<List<Long>>
}