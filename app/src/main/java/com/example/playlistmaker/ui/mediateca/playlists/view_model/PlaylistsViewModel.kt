package com.example.playlistmaker.ui.mediateca.playlists.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.ShowPlaylistsUseCase
import com.example.playlistmaker.ui.mediateca.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val showPlaylistsUseCase: ShowPlaylistsUseCase
) : ViewModel() {

    private val playlists = mutableListOf<Playlist>()

    private val mutableLiveDataPlaylists = MutableLiveData<List<Playlist>>(playlists)
    val liveDataPlaylists: LiveData<List<Playlist>> = mutableLiveDataPlaylists

    private val mutableLiveDataPlaylistsStatus = MutableLiveData<Status<List<Playlist>>>()
    val liveDataPlaylistsStatus: LiveData<Status<List<Playlist>>> = mutableLiveDataPlaylistsStatus

    private val handler = Handler(Looper.getMainLooper())

    fun showPlaylists() {
        playlists.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showPlaylistsUseCase.execute().collect {
                playlists.addAll(it)
                if (playlists.isEmpty()) {
                    mutableLiveDataPlaylistsStatus.postValue(Status.Empty())
                } else {
                    mutableLiveDataPlaylistsStatus.postValue(Status.Content(playlists))
                    mutableLiveDataPlaylists.postValue(playlists)
                }
                Log.d("FavoritesViewModel", "Playlists are ${it.map { it.title }}")
            }
        }
    }

    fun showPlaylistsWithDelay() {
        handler.postDelayed(
            Runnable {
                showPlaylists()
                Log.d("FavoritesViewModel", "Запрашиваю плейлисты с задержкой в 100 millis")
            },
            100
        )
    }

}