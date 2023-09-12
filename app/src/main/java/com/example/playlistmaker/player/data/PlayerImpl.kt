package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.player.view_model.PlayerState

class PlayerImpl(private var mediaPlayer:MediaPlayer) : Player {

    private var state = PlayerState.DEFAULT
    private var stateCallback: ((PlayerState) -> Unit)? = null

    override fun prepare(previewUrl: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            updateState(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            updateState(PlayerState.PREPARED)
        }
    }

    override fun start() {
        mediaPlayer.start()
        updateState(PlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer.pause()
        updateState(PlayerState.PAUSED)
    }

    override fun release() {
        mediaPlayer.release()
        updateState(PlayerState.DEFAULT)
    }

    override fun setStateCallback(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getState(): PlayerState {
        return state
    }

    private fun updateState(newState: PlayerState) {
        state = newState
        stateCallback?.invoke(state)
    }

}