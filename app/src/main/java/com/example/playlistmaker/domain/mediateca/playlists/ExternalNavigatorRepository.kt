package com.example.playlistmaker.domain.mediateca.playlists

interface ExternalNavigatorRepository {

    fun sendMessage(message: String)
}