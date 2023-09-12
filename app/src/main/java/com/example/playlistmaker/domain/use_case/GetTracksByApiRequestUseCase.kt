package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.api.ApiResponse
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.model.Track
import java.util.concurrent.Executors

class GetTracksByApiRequestUseCase(private val musicApi: MusicApi) {
    private val executor = Executors.newCachedThreadPool()

    fun execute(request: String, consumer: Consumer<List<Track>>) {
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