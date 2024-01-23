package com.example.playlistmaker.ui.mediateca.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.*
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.mediateca.playlists.model.PlaylistInfo
import com.example.playlistmaker.ui.models.TrackRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PlaylistViewModel(
    private val playlistId: Int,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val getAllTracksFromPlaylistUseCase: GetAllTracksFromPlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val showPlaylistsUseCase: ShowPlaylistsUseCase,
    private val removeTrackFromPlaylistsTracksStorageUseCase: RemoveTrackFromPlaylistsTracksStorageUseCase
) : ViewModel() {

    private val mutableLiveDataPlaylist = MutableLiveData<PlaylistInfo>()
    val liveDataPlaylist: LiveData<PlaylistInfo> = mutableLiveDataPlaylist

    private val mutableLiveDataIsClickOnTrackAllowed = MutableLiveData<Boolean>(true)
    val liveDataIsClickOnTrackAllowed: LiveData<Boolean> = mutableLiveDataIsClickOnTrackAllowed

    init {
        getPlaylistInfo()
    }

    private fun getPlaylistInfo() {
        viewModelScope.launch(Dispatchers.IO) {

            val playlist = getPlaylistByIdUseCase.execute(playlistId)

            getAllTracksFromPlaylistUseCase.execute(playlist.tracksId).collect {
                val tracks = it.map{ Mapper.mapTrackToTrackRepresentation(it) }
                val playlistDurationMillis = it.sumOf { convertTrackTimeToMillis(it.trackTime) }
                val playlistDuration = convertMillisToMinutes(playlistDurationMillis)

                mutableLiveDataPlaylist.postValue(
                    PlaylistInfo(
                        playlist = playlist,
                        tracks = tracks,
                        durationInMinutes = playlistDuration
                    )
                )
            }
        }
    }

    fun deleteTrack(trackRepresentation: TrackRepresentation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeTrackFromPlaylist(trackRepresentation)
            removeTrackFromPlaylistsTracks(trackRepresentation)
            getPlaylistInfo()
        }
    }

    private suspend fun removeTrackFromPlaylist(trackRepresentation: TrackRepresentation) {
        updatePlaylistUseCase.execute(
            playlist = liveDataPlaylist.value?.playlist!!,
            track = Mapper.mapTrackRepresentationToTrack(trackRepresentation)
        )
    }

    private suspend fun removeTrackFromPlaylistsTracks(trackRepresentation: TrackRepresentation) {

        showPlaylistsUseCase.execute().collect {
            var somePlaylistsContainTrack = false

            it.forEach {
                if (it.tracksId.contains(trackRepresentation.trackId)) {
                    somePlaylistsContainTrack = true
                }
            }

            if(!somePlaylistsContainTrack) {
                removeTrackFromPlaylistsTracksStorageUseCase.execute(
                    track = Mapper.mapTrackRepresentationToTrack(trackRepresentation)
                )
            }
        }
    }

    fun clickOnTrackDebounce() {
        if (liveDataIsClickOnTrackAllowed.value!!) {
            mutableLiveDataIsClickOnTrackAllowed.value = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                mutableLiveDataIsClickOnTrackAllowed.value = true
            }
        }
    }


    private fun convertTrackTimeToMillis(trackTime: String): Long {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.parse(trackTime).time
    }

    private fun convertMillisToMinutes(millis: Long): Int {
        return TimeUnit.MILLISECONDS.toMinutes(millis).toInt()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}