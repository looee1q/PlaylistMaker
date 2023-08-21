package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.databinding.ActivityTrackInfoBinding
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class TrackInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTrackInfoBinding
    private lateinit var track: Track

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var currentTimePlaying = 0

    private var handlerInMainThread = Handler(Looper.getMainLooper())
    private lateinit var updateTrackTimeRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<Track>(intent.extras?.getString(SearchActivity.TRACK)!!)
        bind(track)

        binding.backToSearchActivityButton.setOnClickListener {
            finish()
        }

        preparePlayer()

        updateTrackTimeRunnable = object : Runnable {
            override fun run() {
                currentTimePlaying = mediaPlayer.currentPosition
                Log.d("CURRENT_TIME", "$currentTimePlaying")
                binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimePlaying)
                when (playerState) {
                    STATE_PLAYING -> handlerInMainThread.postDelayed(this, TIME_OF_TRACK_TIME_UPDATE)
                    STATE_PAUSED -> handlerInMainThread.removeCallbacks(this)
                    STATE_PREPARED -> {
                        handlerInMainThread.removeCallbacks(this)
                        binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                        binding.playButton.setImageDrawable(getDrawable(R.drawable.play_button_icon))
                    }
                }
            }
        }

        binding.playButton.setOnClickListener {
            playbackControl()
            handlerInMainThread.post(updateTrackTimeRunnable)
        }

    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun bind(track: Track) {

        with(binding) {
            Glide.with(this@TrackInfoActivity)
                .load(track.artworkUrl512)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.track_icon_mock))
                .centerCrop()
                .transform(roundedCorners(RADIUS_OF_TRACK_COVER_TRACK_CORNER, resources))
                .into(binding.trackCover)

            trackNameTitle.text = track.trackName
            artistNameTitle.text = track.artistName
            trackDuration.text = track.getDuration()

            if (track.collectionName != null) {
                trackAlbum.text = track.collectionName
            } else {
                trackAlbum.visibility = View.GONE
                albumSection.visibility = View.GONE
            }
            trackYear.text = track.getYear()
            trackGenre.text = track.genre
            trackCountry.text = track.country
        }
    }

    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        }
    }

    private fun start() {
        mediaPlayer.start()
        Log.d("PLAYER_STATE", "Начато (продолжено) воспроизведение трека")
        binding.playButton.setImageDrawable(getDrawable(R.drawable.pause_button_icon))
        playerState = STATE_PLAYING
    }

    private fun pause() {
        mediaPlayer.pause()
        Log.d("PLAYER_STATE", "Воспроизведение трека приостановлено")
        binding.playButton.setImageDrawable(getDrawable(R.drawable.play_button_icon))
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pause()
            STATE_PAUSED, STATE_PREPARED -> start()
        }
    }

    companion object {
        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIME_OF_TRACK_TIME_UPDATE = 500L
    }

}