package com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations

import com.example.playlistmaker.domain.search.api.ApiResponse
import com.example.playlistmaker.domain.search.api.MusicApi
import com.example.playlistmaker.domain.search.consumer.Consumer
import com.example.playlistmaker.domain.search.consumer.ConsumerData
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import java.util.concurrent.Executors

class GetTracksByApiRequestUseCaseImpl(private val musicApi: MusicApi) :
    GetTracksByApiRequestUseCase {
    private val executor = Executors.newCachedThreadPool()

    override fun execute(request: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            val tracksResponse = musicApi.getTracks(request)
            when (tracksResponse) {
                is ApiResponse.Success -> {
                    val tracks = tracksResponse.data
                    consumer.consume(ConsumerData.Data(tracks))
                }
                is ApiResponse.Error -> {
                    consumer.consume(ConsumerData.Error())
                }
                is ApiResponse.EmptyResponse -> {
                    consumer.consume(ConsumerData.EmptyData())
                }
            }
        }

    }
}