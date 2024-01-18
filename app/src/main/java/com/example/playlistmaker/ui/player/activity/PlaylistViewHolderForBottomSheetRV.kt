package com.example.playlistmaker.ui.player.activity

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistSmallUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlaylistViewHolderForBottomSheetRV(
    private val binding: PlaylistSmallUnitBinding
) : ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.apply {
            Glide.with(itemView)
                .load(playlist.coverUri)
                .apply(
                    RequestOptions().placeholder(R.drawable.track_icon_mock)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corners_for_small_covers))
                )
                .into(cover)
            title.text = playlist.title
            size.text = itemView.resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }
    }
}