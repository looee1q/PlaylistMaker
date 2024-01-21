package com.example.playlistmaker.ui.mediateca.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.GetAllTracksFromPlaylistUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.GetPlaylistByIdUseCase
import com.example.playlistmaker.ui.mediateca.playlists.model.PlaylistInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlaylistViewModel(
    private val playlistId: Int,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val getAllTracksFromPlaylistUseCase: GetAllTracksFromPlaylistUseCase
) : ViewModel() {

    private val mutableLiveDataPlaylist = MutableLiveData<PlaylistInfo>()
    val liveDataPlaylist: LiveData<PlaylistInfo> = mutableLiveDataPlaylist

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val playlist = getPlaylistByIdUseCase.execute(playlistId)
            var playlistDuration = ""
            getAllTracksFromPlaylistUseCase.execute(playlist.tracksId).collect {
                val a = it.sumOf { convertTrackTimeToMillis(it.trackTime) }
                playlistDuration = convertMillisToTrackTime(a)
            }

            mutableLiveDataPlaylist.postValue(
                PlaylistInfo(
                    playlist = playlist,
                    duration = playlistDuration
                )
            )
        }
    }

    private fun convertTrackTimeToMillis(trackTime: String): Long {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.parse(trackTime).time
    }

    private fun convertMillisToTrackTime(millis: Long): String {
        val dateFormat = SimpleDateFormat("mm", Locale.getDefault())
        return dateFormat.format(millis)
    }

}