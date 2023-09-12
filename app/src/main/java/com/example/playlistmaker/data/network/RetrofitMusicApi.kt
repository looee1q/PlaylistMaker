package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.dto.ITunesServerResponse
import com.example.playlistmaker.data.mapper.TrackToTrackDtoMapper
import com.example.playlistmaker.domain.api.ApiResponse
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.model.Track

class RetrofitMusicApi : MusicApi {
    override fun getTracks(tracksNameRequest: String): ApiResponse<List<Track>> {
        try {
            val retrofitResponse = RetrofitClient.api.search(tracksNameRequest).execute()
            return when {
                !retrofitResponse.isSuccessful -> ApiResponse.Error<List<Track>>()

                retrofitResponse.body() != null -> {
                    val responseBody = retrofitResponse.body() as ITunesServerResponse
                    if (responseBody.results.isEmpty()) {
                        ApiResponse.EmptyResponse<List<Track>>()
                    } else {
                        ApiResponse.Success<List<Track>>(TrackToTrackDtoMapper.map(responseBody))
                    }
                }

                else -> ApiResponse.Error<List<Track>>()
            }
        } catch (exception: Exception) {
            Log.d("Error", "${exception.message}")
            return ApiResponse.Error<List<Track>>()
        }
    }


}
