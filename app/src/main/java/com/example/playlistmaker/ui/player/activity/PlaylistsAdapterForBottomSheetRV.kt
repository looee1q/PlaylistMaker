package com.example.playlistmaker.ui.player.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistSmallUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlaylistsAdapterForBottomSheetRV(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<PlaylistViewHolderForBottomSheetRV>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolderForBottomSheetRV {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolderForBottomSheetRV(PlaylistSmallUnitBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolderForBottomSheetRV, position: Int) {
        holder.bind(playlists[position])
    }
}