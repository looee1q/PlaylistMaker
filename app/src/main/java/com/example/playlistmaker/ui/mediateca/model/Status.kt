package com.example.playlistmaker.ui.mediateca.model

sealed interface Status<T> {

    class Empty<T>(val data: T? = null) : Status<T>

    data class Content<T>(val data: T) : Status<T>
}