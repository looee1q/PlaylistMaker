package com.example.playlistmaker.domain.db.use_cases.interfaces

import kotlinx.coroutines.flow.Flow

interface GetTracksIDsFromDBUseCase {

    fun execute(): Flow<List<Long>>
}