package com.example.playlistmaker.ui.mediateca.playlists.view_model

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddPlaylistUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.SaveCoverToExternalStorageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val saveCoverToExternalStorageUseCase: SaveCoverToExternalStorageUseCase
) : ViewModel() {

    private val mutableLiveDataPlaylistUri = MutableLiveData<Uri?>(null)
    val liveDataPlaylistUri: LiveData<Uri?> = mutableLiveDataPlaylistUri

    fun createPlaylist(title: String, description: String? = null, coverUri: Uri? = liveDataPlaylistUri.value) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistUseCase.execute(
                Playlist(
                    title = title,
                    description = description,
                    coverUri = saveCoverToExternalStorage(coverUri),
                )
            )
        }
    }

    fun setUri(uri: Uri) {
        mutableLiveDataPlaylistUri.value = uri
    }

    private suspend fun saveCoverToExternalStorage(coverUri: Uri?) : Uri? {
        return if (coverUri == null) {
            null
        } else {
            saveCoverToExternalStorageUseCase.execute(coverUri.toString()).toUri()
        }
    }
}