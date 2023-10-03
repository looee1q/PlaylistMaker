package com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.model.Track

interface GetTracksByApiRequestUseCase {
    fun execute(request: String, consumer: Consumer<List<Track>>)
}