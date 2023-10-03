package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityTrackInfoBinding
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.TrackActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.roundedCorners
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class TrackInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTrackInfoBinding
    private lateinit var track: TrackActivity

    private val trackFactory by lazy { Creator.getFactoryForTrack(Mapper.mapTrackActivityToTrack(track)) }

    private val preparePlayerUseCase by lazy { trackFactory.providePreparePlayerUseCase() }
    private val pauseTrackUseCase by lazy { trackFactory.providePauseTrackUseCase() }
    private val playTrackUseCase by lazy { trackFactory.providePlayTrackUseCase() }
    private val playbackControlUseCase by lazy { trackFactory.providePlaybackControlUseCase() }
    private val getPlayingTrackTimeUseCase by lazy { trackFactory.provideGetPlayingTrackTimeUseCase() }
    private val getPlayerStateUseCase by lazy { trackFactory.provideGetPlayerStateUseCase() }
    private val destroyPlayerUseCase by lazy { trackFactory.provideDestroyPlayerUseCase() }

    lateinit var playerState : PlayerState
    private var currentTimePlayingMillis = 0

    private var handlerInMainThread = Handler(Looper.getMainLooper())
    private lateinit var updateTrackTimeRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<TrackActivity>(intent.extras?.getString(SearchActivity.TRACK)!!)
        bind(track)

        binding.backToSearchActivityButton.setOnClickListener {
            finish()
        }

        playerState = preparePlayerUseCase.execute()

        updateTrackTimeRunnable = createTimeUpdaterRunnable()

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
        destroyPlayerUseCase.execute()
    }

    private fun bind(track: TrackActivity) {

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

    private fun start() {
        playerState = playTrackUseCase.execute {
            Log.d("PLAYER_STATE", "Начато (продолжено) воспроизведение трека")
            binding.playButton.setImageDrawable(getDrawable(R.drawable.pause_button_icon))
        }
    }

    private fun pause() {
        playerState = pauseTrackUseCase.execute {
            Log.d("PLAYER_STATE", "Воспроизведение трека приостановлено")
            binding.playButton.setImageDrawable(getDrawable(R.drawable.play_button_icon))
        }
    }

    private fun playbackControl() {
        playerState = playbackControlUseCase.execute(
            doActionWhileOnPause = { pause() }, doActionWhilePlaying = { start() }
        )
    }

    fun createTimeUpdaterRunnable() : Runnable {
        return object : Runnable {
            override fun run() {
                currentTimePlayingMillis = getPlayingTrackTimeUseCase.execute()
                playerState = getPlayerStateUseCase.execute()
                Log.d("PlayerState","$playerState")
                Log.d("CURRENT_TIME", "$currentTimePlayingMillis")
                binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimePlayingMillis)
                when (playerState) {
                    PlayerState.PLAYING -> handlerInMainThread.postDelayed(this, TRACK_TIME_UPDATE_FREQUENCY_MILLIS)
                    PlayerState.PAUSED -> handlerInMainThread.removeCallbacks(this)
                    PlayerState.PREPARED -> {
                        handlerInMainThread.removeCallbacks(this)
                        binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                        binding.playButton.setImageDrawable(getDrawable(R.drawable.play_button_icon))
                    }
                    PlayerState.DEFAULT -> {}
                }
            }
        }
    }

    companion object {
        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f

        private const val TRACK_TIME_UPDATE_FREQUENCY_MILLIS = 500L
    }

}
