package com.example.playlistmaker.ui.mediateca.playlists.view_model

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.GetPlaylistByIdUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.SaveCoverToExternalStorageUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.UpdatePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(
    private val playlistId: Int,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val saveCoverToExternalStorageUseCase: SaveCoverToExternalStorageUseCase
): ViewModel() {

    private val mutableLiveDataPlaylist = MutableLiveData<Playlist>()
    val liveDataPlaylist: LiveData<Playlist> get() = mutableLiveDataPlaylist

    private val mutableLiveDataPlaylistBeforeEditing = MutableLiveData<Playlist>()
    val liveDataPlaylistBeforeEditing: MutableLiveData<Playlist> get() = mutableLiveDataPlaylistBeforeEditing

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistByIdUseCase.execute(playlistId).also {
                mutableLiveDataPlaylist.postValue(it)
                mutableLiveDataPlaylistBeforeEditing.postValue(it)
            }
        }
    }

    fun updatePlaylist() {
        if (liveDataPlaylist.value != liveDataPlaylistBeforeEditing.value) {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d("PlaylistEditorViewModel","Сейчас буду айпдейтить плейлист")
                updatePlaylistUseCase.execute(liveDataPlaylist.value!!)
            }
        }
    }

    fun setNewPlaylistTitle(newTitle: String) {
        val playlistWithUpdatedTitle = liveDataPlaylist.value!!.copy(
            title = newTitle
        )
        mutableLiveDataPlaylist.value = playlistWithUpdatedTitle
    }

    fun setNewPlaylistDescription(newDescription: String) {
        val playlistWithUpdatedDescription = liveDataPlaylist.value!!.copy(
            description = newDescription
        )
        mutableLiveDataPlaylist.value = playlistWithUpdatedDescription
    }

    fun setNewPlaylistUri(newUri: Uri) {
        val playlistWithUpdatedUri = liveDataPlaylist.value!!.copy(
            coverUri = newUri
        )
        mutableLiveDataPlaylist.value = playlistWithUpdatedUri
    }

    fun saveCoverToExternalStorage() {
        if (liveDataPlaylist.value?.coverUri != liveDataPlaylistBeforeEditing.value?.coverUri) {
            Log.d("PlaylistEditorFragment", "Сейчас буду обновлять Uri")
            viewModelScope.launch {
                setNewPlaylistUri(
                    saveCoverToExternalStorageUseCase.execute(liveDataPlaylist.value?.coverUri.toString()).toUri()
                )
            }
            //File(viewModel.liveDataPlaylistBeforeEditing.value?.coverUri?.path!!).delete()
        }
    }

}