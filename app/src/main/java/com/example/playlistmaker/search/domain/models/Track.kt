package com.example.playlistmaker.search.domain.models

import java.io.Serializable

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
    fun getCoverArtWork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}