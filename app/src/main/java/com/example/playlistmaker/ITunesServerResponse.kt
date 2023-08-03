package com.example.playlistmaker

@kotlinx.serialization.Serializable
class ITunesServerResponse(val resultCount: Int, val results: List<Track>)