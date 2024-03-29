package com.example.playlistmaker.search.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(private val coverResolution: String = "100") :
    RecyclerView.Adapter<TrackViewHolder>() {
    internal var tracks = mutableListOf<Track>()
    var onItemClick: ((Track) -> Unit)? = null
    var onLongItemClick: ((Track) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view, coverResolution)
    }

    override fun getItemCount(): Int = tracks.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tracks[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(tracks[position])
            return@setOnLongClickListener true
        }
    }

}