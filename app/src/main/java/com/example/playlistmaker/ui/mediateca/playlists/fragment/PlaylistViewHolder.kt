package com.example.playlistmaker.ui.mediateca.playlists.fragment

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFullUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlaylistViewHolder(private val binding: PlaylistFullUnitBinding) : ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.apply {
            Glide.with(itemView)
                .load(playlist.coverUri)
                .signature(ObjectKey(playlist.id))
                .apply(
                    RequestOptions().placeholder(R.drawable.track_icon_mock)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corners_for_big_covers))
                )
                .into(cover)
            title.text = playlist.title
            size.text = itemView.context.resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }
    }
}