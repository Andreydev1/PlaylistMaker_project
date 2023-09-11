package com.example.playlistmaker.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.player.domain.models.TrackPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val player: Player) : ViewModel() {
    companion object {
        private const val PLAYING_TIME_UPDATING_DELAY = 300L
        }

    private val stateLiveData = MutableLiveData<TrackPlayerState>()
    fun observeState(): LiveData<TrackPlayerState> = stateLiveData

    private val currentTimeLiveData = MutableLiveData<String>()
    fun observeCurrentTime(): LiveData<String> = currentTimeLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val timeRunnable = Runnable { startUpdateTime() }

    init {
        player.setStateCallback { playerState ->
            when (playerState) {
                PlayerState.PREPARED -> {
                    handler.removeCallbacks(timeRunnable)
                    renderState(TrackPlayerState.Prepared)
                }
                PlayerState.PLAYING -> {
                    renderState(TrackPlayerState.Playing)
                    startUpdateTime()
                }
                PlayerState.PAUSED -> {
                    renderState(TrackPlayerState.Paused)
                }
                PlayerState.DEFAULT ->
                    renderState(TrackPlayerState.Default)
            }
        }
    }

    private fun startUpdateTime() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (player.getState() == PlayerState.PLAYING) {
                        updateCurrentTime()
                        handler.postDelayed(
                            this,
                            PLAYING_TIME_UPDATING_DELAY
                        )
                    }
                }
            },
            PLAYING_TIME_UPDATING_DELAY
        )
    }

    fun switchPlayPause() {
        when (player.getState()) {
            PlayerState.PLAYING -> {
                player.pause()
                renderState(TrackPlayerState.Paused)
                startUpdateTime()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                player.start()
                renderState(TrackPlayerState.Playing)
            }
            else -> Unit
        }
    }

    fun pausePlayer() {
        player.pause()
        handler.removeCallbacks(timeRunnable)
    }

    fun releasePlayer() {
        player.release()
        handler.removeCallbacks(timeRunnable)
    }

    fun preparePlayer(previewUrl: String) {
        player.prepare(previewUrl)
    }

    private fun renderState(state: TrackPlayerState) {
        stateLiveData.postValue(state)
    }

    fun updateCurrentTime() {
        currentTimeLiveData.postValue(
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                player.getCurrentTime()
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}