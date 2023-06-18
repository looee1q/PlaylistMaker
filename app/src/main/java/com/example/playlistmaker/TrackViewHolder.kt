package com.example.playlistmaker

import android.util.DisplayMetrics
import android.util.TypedValue
import android.util.TypedValue.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackViewHolder(itemView: View): ViewHolder(itemView) {

    val trackIcon = itemView.findViewById<ImageView>(R.id.track_icon)
    val trackName = itemView.findViewById<TextView>(R.id.track_name)
    val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    val trackDuration = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(track: Track) {

        fun roundedCorners(radius: Float) = RoundedCorners(
            applyDimension(
                COMPLEX_UNIT_DIP,
                radius,
                itemView.resources.displayMetrics).toInt()
        )

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .transform(roundedCorners(2f))
            .centerCrop()
            .into(trackIcon)
        
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.trackTime
    }
}