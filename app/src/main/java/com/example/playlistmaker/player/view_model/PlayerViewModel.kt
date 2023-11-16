package com.example.playlistmaker.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.favorites.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.favorites.domain.models.PlaylistState
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.playlists.domain.models.InsertTrackInPlaylistState
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val player: Player,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private val favoriteState = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = favoriteState

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private val resultOfInsert = MutableLiveData<InsertTrackInPlaylistState>()
    fun observeInsertTrackInPlaylist(): LiveData<InsertTrackInPlaylistState> = resultOfInsert

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
        timerJob?.cancel()
    }

    fun releasePlayer() {
        player.release()
    }

    fun preparePlayer(previewUrl: String?) {
        player.prepare(previewUrl)
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    private fun renderFavoriteState(inFavorite: Boolean) {
        favoriteState.postValue(inFavorite)
    }

    fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            viewModelScope.launch {
                favoritesInteractor.deleteTrack(track)
                track.isFavorite = false
                renderFavoriteState(track.isFavorite)
            }
        } else {
            viewModelScope.launch {
                favoritesInteractor.saveTrack(track)
                track.isFavorite = true
                renderFavoriteState(track.isFavorite)
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { list ->
                    val playlistsList = mutableListOf<Playlist>()
                    playlistsList.addAll(list)
                    if (playlistsList.isNotEmpty()) {
                        stateLiveData.postValue(PlaylistState.Success(playlistsList))
                    } else {
                        stateLiveData.postValue(PlaylistState.Empty)
                    }
                }
        }

    }

    fun addTrackInPlaylist(track: Track, playlist: Playlist) {
        if (playlist.id != null) {
            viewModelScope.launch {
                val playlistIds = playlistInteractor.getPlaylistsIdsContainTrack(track)
                if (playlistIds.contains(playlist.id)) {
                    resultOfInsert.postValue(InsertTrackInPlaylistState.Error("Трек уже добавлен в плейлист " + playlist.name))

                } else {
                    playlistInteractor.addTrackInPlaylist(track, playlist)
                    resultOfInsert.postValue(InsertTrackInPlaylistState.Success("Добавлено в плейлист " + playlist.name))
                }
            }
        }
    }


    companion object {
        private const val PLAYING_TIME_UPDATING_DELAY = 300L
    }
}





