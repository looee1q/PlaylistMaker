package com.example.playlistmaker

import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.util.TypedValue.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private const val RADIUS_OF_ICON_TRACK_CORNER: Float = 2f

class TrackViewHolder(itemView: View): ViewHolder(itemView) {

    private val trackIcon = itemView.findViewById<ImageView>(R.id.track_icon)
    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    private val trackDuration = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(track: Track) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .centerCrop()
            .transform(roundedCorners(RADIUS_OF_ICON_TRACK_CORNER, itemView.resources))
            .into(trackIcon)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.getDuration()
    }

}