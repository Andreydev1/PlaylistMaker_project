package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.PlayerState


interface PlayerManager {
    fun prepare(previewUrl:String)
    fun start()
    fun pause()
    fun release()
    fun playerStateCallBack(callback: (PlayerState) -> Unit)
    fun getCurrentTime():Int
    fun getPlayerState() : PlayerState
}