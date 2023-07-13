package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val trackList: MutableList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    var listener: (Track) -> Unit = {Log.d("Listener", "Пустышка")}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track_unit, parent, false)
        return TrackViewHolder(view)
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