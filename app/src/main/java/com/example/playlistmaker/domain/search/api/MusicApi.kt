package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicApi {

    fun getTracks(tracksNameRequest: String): Flow<ApiResponse<List<Track>>>
}