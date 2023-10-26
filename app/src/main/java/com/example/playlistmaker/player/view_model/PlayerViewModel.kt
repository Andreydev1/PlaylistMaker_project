package com.example.playlistmaker.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.player.domain.models.PlayerState


import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(private val player: Player) : ViewModel() {
    companion object {
        private const val PLAYING_TIME_UPDATING_DELAY = 300L
    }

    private var timerJob: Job? = null


    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState


    init {
        player.setStateCallback { playerState ->
            renderState(playerState)
        }
    }


    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> {}
        }
    }

    private fun startUpdateTime() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying()) {
                delay(PLAYING_TIME_UPDATING_DELAY)
                if (playerState.value is PlayerState.Playing)
                    playerState.postValue(PlayerState.Playing(player.getCurrentPlayerPosition()))
            }
        }
    }

    private fun startPlayer() {
        player.start()
        startUpdateTime()
    }

    fun pausePlayer() {
        player.pause()
    }

    fun releasePlayer() {
        player.release()
    }

    fun preparePlayer(previewUrl: String) {
        player.prepare(previewUrl)
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }
}

