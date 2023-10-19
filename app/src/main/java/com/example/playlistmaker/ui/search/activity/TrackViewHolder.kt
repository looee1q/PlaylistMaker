package com.example.playlistmaker.ui.search.activity

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackUnitBinding
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.roundedCorners

class TrackViewHolder(private val binding: ActivityTrackUnitBinding): ViewHolder(binding.root) {

    fun bind(track: TrackRepresentation) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .centerCrop()
            .transform(roundedCorners(RADIUS_OF_ICON_TRACK_CORNER, itemView.resources))
            .into(binding.trackIcon)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = track.trackTime
    }

    companion object {
        private const val RADIUS_OF_ICON_TRACK_CORNER: Float = 2f
    }
}