package com.example.playlistmaker.data.search.network

import android.util.Log
import com.example.playlistmaker.data.search.dto.ITunesServerResponse
import com.example.playlistmaker.data.search.mapper.Mapper
import com.example.playlistmaker.domain.search.api.ApiResponse
import com.example.playlistmaker.domain.search.api.MusicApi
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
class RetrofitMusicApi : MusicApi {
    override fun getTracks(tracksNameRequest: String): Flow<ApiResponse<List<Track>>> = flow {
        try {
            val retrofitResponse = RetrofitClient.api.search(tracksNameRequest)
            when {
                !retrofitResponse.isSuccessful -> emit(ApiResponse.Error<List<Track>>())

                retrofitResponse.body() != null -> {
                    val responseBody = retrofitResponse.body() as ITunesServerResponse
                    if (responseBody.results.isEmpty()) {
                        emit(ApiResponse.EmptyResponse<List<Track>>())
                    } else {
                        emit(
                            ApiResponse.Success<List<Track>>(Mapper.mapITunesServerResponseToListOfTracks(responseBody))
                        )
                    }
                }

                else -> emit(ApiResponse.Error<List<Track>>())
            }
        } catch (exception: Exception) {
            Log.d("Error", "${exception.message}")
            emit(ApiResponse.Error<List<Track>>())
        }

    }.flowOn(Dispatchers.IO)

}

