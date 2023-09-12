package com.example.playlistmaker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.data.AndroidInteractor
import com.example.playlistmaker.data.MediaPlayerC
import com.example.playlistmaker.data.UIInteractor
import com.example.playlistmaker.data.TrackPlayerRepositoryImpl
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.databinding.ActivityTrackInfoBinding
import com.example.playlistmaker.domain.*
import com.example.playlistmaker.presentation.ui.SearchActivity
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class TrackInfoA: AppCompatActivity() {

    private lateinit var binding: ActivityTrackInfoBinding
    private lateinit var track: TrackDto

    private lateinit var playTrackUseCase: PlayTrackUseCase
    private lateinit var pauseTrackUseCase: PauseTrackUseCase
    private lateinit var playbackControlUseCase: PlaybackControlUseCase
    private lateinit var destroyMediaPlayerUseCase: DestroyMediaPlayerUseCase
    private lateinit var updateTrackTimeUseCase: UpdateTrackTimeUseCase

    private val uiInteractor = object : UIInteractor {
        override fun setPauseButtonIcon(drawableId: Int) {
            binding.playButton.setImageDrawable(getDrawable(drawableId))
        }

        override fun setPlayButtonIcon(drawableId: Int) {
            binding.playButton.setImageDrawable(getDrawable(drawableId))
        }

        override fun setTrackTimeWhilePlaying(timeMillis: Int) {
            binding.playingProgressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
        }
    }

    private val androidInteractor = object : AndroidInteractor {
        override fun getMainThreadHandler(): Handler {
            return Handler(Looper.getMainLooper())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<TrackDto>(intent.extras?.getString(SearchActivity.TRACK)!!)
        bind(track)

        val mediaPlayerC = MediaPlayerC(track = track, uiInteractor = uiInteractor, androidInteractor = androidInteractor)
        val trackPlayerRepositoryImpl = TrackPlayerRepositoryImpl(mediaPlayerC)

        playTrackUseCase = PlayTrackUseCase(trackPlayerRepositoryImpl)
        pauseTrackUseCase = PauseTrackUseCase(trackPlayerRepositoryImpl)
        playbackControlUseCase = PlaybackControlUseCase(trackPlayerRepositoryImpl)
        destroyMediaPlayerUseCase = DestroyMediaPlayerUseCase(trackPlayerRepositoryImpl)
        updateTrackTimeUseCase = UpdateTrackTimeUseCase(trackPlayerRepositoryImpl)

        PreparePlayerUseCase(trackPlayerRepositoryImpl).execute()

        binding.backToSearchActivityButton.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            playbackControlUseCase.execute(track)
            updateTrackTimeUseCase.execute(track)
        }

    }

    override fun onPause() {
        super.onPause()
        pauseTrackUseCase.execute(track)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyMediaPlayerUseCase.execute()
    }

    private fun bind(track: TrackDto) {

        with(binding) {
            Glide.with(this@TrackInfoA)
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

    companion object {
        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f
    }

}