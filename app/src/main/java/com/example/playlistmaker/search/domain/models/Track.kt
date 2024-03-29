package com.example.playlistmaker.search.domain.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false
): Serializable{
    fun getCoverArtWork(resolution: String) =
        artworkUrl100.replaceAfterLast('/', "${resolution}x${resolution}bb.jpg")

    fun getTrackTimeResulted() =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime?.toInt() ?: 0)
}