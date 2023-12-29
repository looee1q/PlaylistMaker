package com.example.playlistmaker.ui.mediateca.playlists.fragment

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.roundedCorners

class PlaylistViewHolder(private val binding: FragmentPlaylistUnitBinding) : ViewHolder(binding.root) {

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

    private fun showNumberOfTracksInCorrectDeclension(numberOfTracks: Int): String {
        return when {
            (numberOfTracks%100) >= 11 && (numberOfTracks%100) <= 19 -> "$numberOfTracks треков"
            (numberOfTracks%10) == 1 -> "$numberOfTracks трек"
            (numberOfTracks%10) >= 2 && (numberOfTracks%10) <= 4 -> "$numberOfTracks трека"
            else -> "$numberOfTracks треков"
        }
    }

    companion object {
        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 8f
    }
}