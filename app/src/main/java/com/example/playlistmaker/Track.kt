package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName:String,
    val country: String
): Serializable{
    fun getCoverArtWork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
