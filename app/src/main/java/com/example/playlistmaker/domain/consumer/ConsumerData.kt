package com.example.playlistmaker.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val data: T) : ConsumerData<T>
    class EmptyData<T>() : ConsumerData<T>
    class Error<T>() : ConsumerData<T>
}