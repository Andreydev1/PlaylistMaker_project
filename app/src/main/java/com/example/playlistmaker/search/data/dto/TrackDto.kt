package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName


data class TrackDto(
    @SerializedName("trackId") val id: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?
)