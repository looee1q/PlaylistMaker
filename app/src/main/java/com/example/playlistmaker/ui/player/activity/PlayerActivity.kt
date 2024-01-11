package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.roundedCorners
import com.example.playlistmaker.ui.mediateca.playlists.fragment.PlaylistCreatorFragment
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackRepresentation

    private lateinit var adapter: PlaylistsAdapterForBottomSheetRV
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val playerViewModel: PlayerViewModel by viewModel { parametersOf(track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle","onCreate || PlayerActivity")

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<TrackRepresentation>(intent.extras?.getString(TRACK)!!)
        bind(track)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).also {
            it.state = BottomSheetBehavior.STATE_HIDDEN

            it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }
                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) { }
            })
        }

        adapter = PlaylistsAdapterForBottomSheetRV(playerViewModel.liveDataPlaylists.value!!)
        binding.recyclerViewPlaylists.adapter = adapter
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.backArrowButton.setOnClickListener {
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

        binding.addToPlaylistButton.setOnClickListener {
            playerViewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        playerViewModel.liveDataPlaylists.observe(this) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                Log.d("PlayerActivity", "List of playlists ${playerViewModel!!.liveDataPlaylists.value?.map { it.title }}")
                adapter.notifyDataSetChanged()
            }
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val bundle = Bundle().also {
                it.putBoolean("FROM_PLAYER_ACTIVITY", true)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.player_fragment_container, PlaylistCreatorFragment::class.java, bundle)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    this@PlayerActivity.finish()
                }
            }

        })

    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle","onPause || PlayerActivity")
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
        val drawable = if (isTrackFavorite == true) R.drawable.like_button_favorite else R.drawable.like_button_icon
        binding.likeButton.setImageDrawable(getDrawable(drawable))
    }

    //Костыль!!! Удалить при рефакторинге на SingleActivity
    fun showPlaylists(showPlayerLayout: Boolean = true) {
        binding.playerRootLayout.isVisible = showPlayerLayout
        binding.bottomSheet.isVisible = showPlayerLayout
        binding.overlay.isVisible = showPlayerLayout
        if (!showPlayerLayout) {
            playerViewModel.pausePlayer()
        }
    }

    companion object {

        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f

        const val TRACK = "TRACK"

        fun createArgs(encodedTrack: String) : Bundle {
            return bundleOf(TRACK to encodedTrack)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle","onStart || PlayerActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle","onResume || PlayerActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle","onStop || PlayerActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle","onDestroy || PlayerActivity")
    }

}
