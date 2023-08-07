package com.example.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.PlayerManager
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*


class PlayerPresentation(private var view: PlayerView?,
                         private var currentTrack: Track,
                         private val playerManager: PlayerManager) {
    companion object {
        private const val TIMER_DELAY = 500L
    }

    private val playerHandler = Handler(Looper.getMainLooper())
    private val timeRunnable = Runnable {
        updateTime()
    }
    init {
        playerManager.prepare(currentTrack.previewUrl)
        playerManager.playerStateCallBack { playerState ->
            when (playerState) {
                PlayerState.PREPARED -> {
                    playerHandler.removeCallbacks(timeRunnable)
                    view?.setPlayerStatePrepared()
                }
                PlayerState.PLAYING -> {
                    view?.setPlayerStateStart()
                    updateTime()
                }
                PlayerState.PAUSED -> {
                    view?.setPlayerStatePause()
                }
                else -> {}
            }
        }
    }

    private fun updateTime() {
        playerHandler.postDelayed(object : Runnable {
            override fun run() {
                if (playerManager.getPlayerState() == PlayerState.PLAYING) {
                    setTime()
                    playerHandler.postDelayed(this, TIMER_DELAY)
                }
            }
        }, TIMER_DELAY)
    }

    private fun setTime() {
        val currentTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerManager.getCurrentTime())
        view?.setCurrentTime(currentTime)
    }

        fun setTrackInfo() {
            view?.setTrackName(currentTrack.trackName)
            view?.setArtistName(currentTrack.artistName)
            view?.setTrackTime(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(currentTrack.trackTime.toInt())
            )
            view?.setAlbumName(currentTrack.collectionName)
            view?.setReleaseDate(currentTrack.releaseDate.substring(0, 4))
            view?.setGenre(currentTrack.primaryGenreName)
            view?.setCountry(currentTrack.country)
            view?.setCover(currentTrack.getCoverArtWork())
          //  view.setAlbumGroupVisibility(currentTrack.collectionName != "")
        }
    fun switchPlayPause() {
        when (playerManager.getPlayerState()) {
            PlayerState.PLAYING -> {
                playerManager.pause()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                playerManager.start()
            }
            else -> {}
        }
    }

    fun pausePlayer() {
        playerManager.pause()
        playerHandler.removeCallbacks(timeRunnable)
    }

    fun releasePlayer() {
        playerManager.release()
        playerHandler.removeCallbacks(timeRunnable)
    }
   fun onViewDestroyed() {
       view = null
    }
}

