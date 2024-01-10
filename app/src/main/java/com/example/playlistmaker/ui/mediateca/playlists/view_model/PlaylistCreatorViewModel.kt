package com.example.playlistmaker.ui.mediateca.playlists.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(
    private val addPlaylistUseCase: AddPlaylistUseCase
) : ViewModel() {

    private val mutableLiveDataPlaylistUri = MutableLiveData<Uri?>(null)
    val liveDataPlaylistUri: LiveData<Uri?> = mutableLiveDataPlaylistUri

    fun createPlaylist(title: String, description: String? = null, coverUri: Uri? = liveDataPlaylistUri.value) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistUseCase.execute(
                Playlist(
                    title = title,
                    description = description,
                    coverUri = coverUri,
                )
            )
        }
    }

    fun setUri(uri: Uri) {
        mutableLiveDataPlaylistUri.value = uri
    }
}