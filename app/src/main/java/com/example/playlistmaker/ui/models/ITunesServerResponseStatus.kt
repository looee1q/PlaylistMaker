package com.example.playlistmaker.ui.models

import com.example.playlistmaker.domain.search.consumer.ConsumerData

enum class ITunesServerResponseStatus {
    NOTHING_FOUND, CONNECTION_ERROR, SUCCESS, LOADING, EMPTY_REQUEST
}