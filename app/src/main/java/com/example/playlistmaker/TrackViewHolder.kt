package com.example.playlistmaker

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners



class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    @RequiresApi(Build.VERSION_CODES.S)
    fun bind(model: Track){
        trackName.text = model.trackName
        trackTime.text = model.trackTime
        artistName.text = model.artistName

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_rounded_corners)))
            .placeholder(R.drawable.ic_no_internet)
            .into(trackImage)
    }
}