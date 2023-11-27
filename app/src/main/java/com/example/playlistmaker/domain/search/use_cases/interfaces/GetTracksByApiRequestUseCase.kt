package com.example.playlistmaker.domain.search.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface GetTracksByApiRequestUseCase {
    fun execute(request: String): Flow<ApiResponse<List<Track>>>
}