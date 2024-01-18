package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.databinding.PlaylistFullUnitBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlaylistsAdapter(private val playlists: List<Playlist>) : Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistFullUnitBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}