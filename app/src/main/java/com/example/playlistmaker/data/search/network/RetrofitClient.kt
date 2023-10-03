package com.example.playlistmaker.data.search.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {

    private const val BASE_ITUNES_URL: String = "https://itunes.apple.com"

    private val contentType = "application/json".toMediaType()
    private val converterFactory = Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_ITUNES_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    val api: ITunesApi by lazy {
        retrofit.create(ITunesApi::class.java)
    }

}