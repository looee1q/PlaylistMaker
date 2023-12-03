package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.roundedCorners
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackRepresentation

    private val playerViewModel: PlayerViewModel by viewModel { parametersOf(track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<TrackRepresentation>(intent.extras?.getString(TRACK)!!)
        bind(track)

        binding.backToSearchActivityButton.setOnClickListener {
            finish()
        }

        playerViewModel.liveDataPlayerState.observe(this) {
            playerStateRender(it)
            Log.d("StateInsideObserver", "State inside observer is ${it}")
        }

        playerViewModel.liveDataTrackPlaybackProgress.observe(this) {
            playbackTimeRender(it)
        }

        binding.playButton.setOnClickListener {
            playerViewModel.playbackControl()
            playerViewModel.startTimeUpdater()
        }

        playerViewModel.liveDataIsTrackFavorite.observe(this) {
            setLikeButtonState(it)
        }

        binding.likeButton.setOnClickListener {
            playerViewModel.changeTrackFavoriteStatus()
        }

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun bind(track: TrackRepresentation) {

        with(binding) {
            Glide.with(this@PlayerActivity)
                .load(track.artworkUrl512)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.track_icon_mock))
                .centerCrop()
                .transform(roundedCorners(RADIUS_OF_TRACK_COVER_TRACK_CORNER, resources))
                .into(binding.trackCover)

            trackNameTitle.text = track.trackName
            artistNameTitle.text = track.artistName
            trackDuration.text = track.trackTime

            if (track.collectionName != null) {
                trackAlbum.text = track.collectionName
            } else {
                trackAlbum.visibility = View.GONE
                albumSection.visibility = View.GONE
            }
            trackYear.text = track.releaseYear
            trackGenre.text = track.genre
            trackCountry.text = track.country
        }
    }

    private fun playerStateRender(state: PlayerState) {

        when (state) {
            PlayerState.PLAYING -> binding.playButton.setImageDrawable(getDrawable(R.drawable.pause_button_icon))
            PlayerState.PAUSED, PlayerState.PREPARED -> binding.playButton.setImageDrawable(getDrawable(R.drawable.play_button_icon))
            PlayerState.DEFAULT -> {}
        }
    }

    private fun playbackTimeRender(currentTime: Int) {

        when (playerViewModel.liveDataPlayerState.value) {
            PlayerState.PLAYING, PlayerState.PAUSED -> {
                binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime)
            }
            PlayerState.PREPARED -> {
                binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }
            PlayerState.DEFAULT, null -> {}
        }
    }

    private fun setLikeButtonState(isTrackFavorite: Boolean?) {
        if (isTrackFavorite == true) {
            binding.likeButton.setImageDrawable(getDrawable(R.drawable.like_button_favorite))
        } else {
            binding.likeButton.setImageDrawable(getDrawable(R.drawable.like_button_icon))
        }
    }

    companion object {

        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f

        const val TRACK = "TRACK"

        fun createArgs(encodedTrack: String) : Bundle {
            return bundleOf(TRACK to encodedTrack)
        }
    }

}
