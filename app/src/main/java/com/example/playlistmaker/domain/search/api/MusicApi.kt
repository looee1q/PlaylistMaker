package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.model.Track

interface MusicApi {

    fun getTracks(tracksNameRequest: String) : ApiResponse<List<Track>>
}