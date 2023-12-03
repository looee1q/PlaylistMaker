package com.example.playlistmaker.domain.search.use_cases.implementations

import com.example.playlistmaker.domain.search.api.ApiResponse
import com.example.playlistmaker.domain.search.api.MusicApi
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import kotlinx.coroutines.flow.Flow

class GetTracksByApiRequestUseCaseImpl(private val musicApi: MusicApi) : GetTracksByApiRequestUseCase {

    override fun execute(request: String): Flow<ApiResponse<List<Track>>> {
        return musicApi.getTracks(request)
    }
}