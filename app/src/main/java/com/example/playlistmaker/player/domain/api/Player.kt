package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.view_model.PlayerState


interface Player {
    fun prepare(previewUrl: String)
    fun start()
    fun pause()
    fun release()
    fun setStateCallback(callback: (PlayerState) -> Unit)
    fun getCurrentTime(): Int
    fun getState(): PlayerState
}