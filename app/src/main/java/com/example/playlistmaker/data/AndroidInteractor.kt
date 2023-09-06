package com.example.playlistmaker.data

import android.os.Handler


interface AndroidInteractor {
    fun getMainThreadHandler() : Handler
}