package com.example.playlistmaker.ui.search.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivityTrackUnitBinding
import com.example.playlistmaker.ui.models.TrackRepresentation

class TrackAdapter(private val trackList: List<TrackRepresentation>): RecyclerView.Adapter<TrackViewHolder>() {

    var listener: (TrackRepresentation) -> Unit = {Log.d("Listener", "Пустышка")}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ActivityTrackUnitBinding.inflate(layoutInflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            Log.d("Listener", "Разбудил")
            listener.invoke(trackList[position])
        }
    }
}