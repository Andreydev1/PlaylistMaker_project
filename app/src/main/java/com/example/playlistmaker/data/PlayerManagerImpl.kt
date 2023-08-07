package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.PlayerManager
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.ui.PlayerActivity

class PlayerManagerImpl : PlayerManager {
    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT
    private var playerStateCallBack: ((PlayerState) -> Unit)? = null

    override fun prepare(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            updatePlayerState(PlayerState.PREPARED)
        }
    }

    override fun start() {
       mediaPlayer.start()
       updatePlayerState(PlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer.pause()
        updatePlayerState(PlayerState.PAUSED)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun playerStateCallBack(callback: (PlayerState) -> Unit) {
        playerStateCallBack = callback
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    private fun updatePlayerState(currentState: PlayerState){
        playerState = currentState
        playerStateCallBack?.invoke(playerState)
    }
}