package com.example.playlistmaker.ui.player.activity

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistSmallUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.roundedCorners
import com.example.playlistmaker.showNumberOfTracksInCorrectDeclension

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
                    roundedCorners(RADIUS_OF_PLAYLIST_COVER_CORNERS, itemView.resources)
                )
                .into(cover)
            title.text = playlist.title
            size.text = showNumberOfTracksInCorrectDeclension(playlist.size)
        }
    }

    companion object {
        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 2f
    }

}