package com.example.playlistmaker

import android.os.Bundle
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

class TrackInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTrackInfoBinding
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Json.decodeFromString<Track>(intent.extras?.getString(SearchActivity.TRACK)!!)
        bind(track)

        binding.backToSearchActivityButton.setOnClickListener {
            finish()
        }

        binding.playingProgressTime.text = "0:00" //Логика выведения времени проигрывания будет дописана позже
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

    companion object {
        private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f
    }

}