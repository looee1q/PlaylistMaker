package com.example.playlistmaker.ui.mediateca.model

sealed interface Status<T> {

    class Empty<T> : Status<T>

    data class Content<T>(val data: T) : Status<T>
}