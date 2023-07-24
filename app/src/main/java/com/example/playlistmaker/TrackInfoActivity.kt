package com.example.playlistmaker

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
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

private const val RADIUS_OF_TRACK_COVER_TRACK_CORNER: Float = 8f

class TrackInfoActivity: AppCompatActivity() {

    lateinit var backToMainMenuArray: ImageButton
    lateinit var playButton: ImageButton
    lateinit var addToPlaylistButton: ImageButton
    lateinit var likeButton: ImageButton

    lateinit var trackCover: ImageView

    lateinit var playingProgressTime: TextView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var trackDuration: TextView
    lateinit var albumSection: TextView
    lateinit var trackAlbum: TextView
    lateinit var trackYear: TextView
    lateinit var trackGenre: TextView
    lateinit var trackCountry: TextView

    lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_track_info)

        backToMainMenuArray = findViewById<ImageButton>(R.id.back_button)
        playButton = findViewById<ImageButton>(R.id.play_button)
        addToPlaylistButton = findViewById<ImageButton>(R.id.add_to_playlist_button)
        likeButton = findViewById<ImageButton>(R.id.like_button)

        playingProgressTime = findViewById(R.id.playing_progress_time)

        trackCover = findViewById(R.id.track_cover)
        trackName = findViewById(R.id.track_name_title)
        artistName = findViewById(R.id.artist_name_title)
        trackDuration = findViewById(R.id.track_duration)
        albumSection = findViewById(R.id.album_section)
        trackAlbum = findViewById(R.id.track_album)
        trackYear = findViewById(R.id.track_year)
        trackGenre = findViewById(R.id.track_genre)
        trackCountry = findViewById(R.id.track_country)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getSerializable(TRACK, Track::class.java)!!
        } else {
            (intent.extras?.getSerializable(TRACK)) as Track
        }
        bind(track)

        backToMainMenuArray.setOnClickListener {
            finish()
        }

        playingProgressTime.text = "0:00" //Логика выведения времени проигрывания будет дописана позже


    }

    private fun bind(track: Track) {

        Glide.with(this)
            .load(track.artworkUrl512)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .transform(roundedCorners(RADIUS_OF_TRACK_COVER_TRACK_CORNER))
            .centerCrop()
            .into(trackCover)

        Log.d("TrackUrl", track.artworkUrl512)

        trackName.text = track.trackName
        artistName.text = track.artistName
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

    private fun roundedCorners(radius: Float) = RoundedCorners(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radius,
            resources.displayMetrics
        ).toInt()
    )

}