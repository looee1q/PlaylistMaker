package com.example.playlistmaker.domain.search.use_cases.interfaces

import com.example.playlistmaker.domain.search.consumer.Consumer
import com.example.playlistmaker.domain.model.Track

interface GetTracksByApiRequestUseCase {
    fun execute(request: String, consumer: Consumer<List<Track>>)
}