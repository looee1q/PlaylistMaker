package com.example.playlistmaker.ui.mediateca.model

import com.example.playlistmaker.ui.models.TrackRepresentation

sealed class FavoriteStatus(open val favoriteTracks: List<TrackRepresentation> = listOf()) {

    data class Empty(override val favoriteTracks: List<TrackRepresentation>) : FavoriteStatus(favoriteTracks)

    data class Content(override val favoriteTracks: List<TrackRepresentation>) : FavoriteStatus(favoriteTracks)
}