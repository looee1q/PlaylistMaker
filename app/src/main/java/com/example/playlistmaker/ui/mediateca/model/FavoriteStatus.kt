package com.example.playlistmaker.ui.mediateca.model

import com.example.playlistmaker.ui.models.TrackRepresentation

sealed interface FavoriteStatus {

    object Empty : FavoriteStatus

    data class Content(val favoriteTracks: MutableList<TrackRepresentation>) : FavoriteStatus
}