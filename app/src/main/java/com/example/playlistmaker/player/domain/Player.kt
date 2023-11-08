package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.models.PlayerState


interface Player {
    fun prepare(previewUrl: String?)
    fun start()
    fun pause()
    fun release()
    fun getCurrentTime(): Int
    fun isPlaying(): Boolean
    fun setStateCallback(callback: (PlayerState) -> Unit)
    fun getCurrentPlayerPosition(): String
}