package com.example.playlistmaker.ui.mediateca.playlists.fragment

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFullUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.roundedCorners

class PlaylistViewHolder(private val binding: PlaylistFullUnitBinding) : ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.apply {
            Glide.with(itemView)
                .load(playlist.coverUri)
                .apply(
                    RequestOptions().placeholder(R.drawable.track_icon_mock)
                )
                .transform(
                    CenterCrop(),
                    roundedCorners(RADIUS_OF_PLAYLIST_COVER_CORNERS, itemView.resources)
                )
                .into(cover)
            title.text = playlist.title
            size.text = itemView.context.resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }
    }

    companion object {
        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 8f
    }
}