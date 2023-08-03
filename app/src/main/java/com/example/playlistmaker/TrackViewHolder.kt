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
import com.example.playlistmaker.databinding.ActivityTrackUnitBinding

class TrackViewHolder(private val binding: ActivityTrackUnitBinding): ViewHolder(binding.root) {

    fun bind(track: Track) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .centerCrop()
            .transform(roundedCorners(RADIUS_OF_ICON_TRACK_CORNER, itemView.resources))
            .into(binding.trackIcon)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = track.getDuration()
    }

    companion object {
        private const val RADIUS_OF_ICON_TRACK_CORNER: Float = 2f
    }
}