package com.example.playlistmaker.presentation

interface PlayerView {
    fun setTrackName(name: String)
    fun setArtistName(name: String)
    fun setTrackTime(time: String)
    fun setAlbumName(name: String)
    fun setReleaseDate(date: String)
    fun setGenre(name: String)
    fun setCountry(name: String)
    fun setCover(coverPath: String)
    fun setPlayerStateStart()
    fun setPlayerStatePause()
    fun setPlayerStatePrepared()
    fun setCurrentTime(time: String)
}