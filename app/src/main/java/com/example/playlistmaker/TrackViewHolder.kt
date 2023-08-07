package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*


class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track){
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime.toInt())
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_rounded_corners)))
            .placeholder(R.drawable.ic_no_internet_track)
            .into(artworkUrl100)

    }
}