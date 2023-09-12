package com.example.playlistmaker.domain.api

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    class EmptyResponse<T>() : ApiResponse<T>
    class Error<T>() : ApiResponse<T>
}