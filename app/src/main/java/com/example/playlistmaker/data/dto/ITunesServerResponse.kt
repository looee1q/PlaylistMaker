package com.example.playlistmaker.data.dto

@kotlinx.serialization.Serializable
class ITunesServerResponse(val resultCount: Int, val results: List<TrackDto>)