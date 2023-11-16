package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.favorites.domain.models.Playlist

class PlaylistListAdapter : RecyclerView.Adapter<PlaylistListViewHolder>() {
    var playlists = mutableListOf<Playlist>()
    var onItemClick: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_list, parent, false)
        return PlaylistListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlists[position])
        }
    }
}