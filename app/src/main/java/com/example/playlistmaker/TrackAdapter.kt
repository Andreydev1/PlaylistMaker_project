package com.example.playlistmaker

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val data: List<Track>):RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
       holder.bind(data[position])
    }

}

