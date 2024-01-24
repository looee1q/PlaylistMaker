package com.example.playlistmaker.data.mediateca

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.mediateca.playlists.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(private val context: Context): ExternalNavigatorRepository {

    override fun sendMessage(message: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser: Intent = Intent.createChooser(shareIntent, "").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
    }
}