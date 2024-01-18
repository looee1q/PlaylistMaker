package com.example.playlistmaker.ui.search.fragment

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackUnitBinding
import com.example.playlistmaker.ui.models.TrackRepresentation

class TrackViewHolder(private val binding: ActivityTrackUnitBinding): ViewHolder(binding.root) {

    fun bind(track: TrackRepresentation) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .apply(RequestOptions()
                .placeholder(R.drawable.track_icon_mock))
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corners_for_small_covers))
            )
            .into(binding.trackIcon)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = track.trackTime
    }
}