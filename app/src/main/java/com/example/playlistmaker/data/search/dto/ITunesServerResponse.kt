package com.example.playlistmaker.data.search.dto

@kotlinx.serialization.Serializable
class ITunesServerResponse(val resultCount: Int, val results: List<TrackDto>)