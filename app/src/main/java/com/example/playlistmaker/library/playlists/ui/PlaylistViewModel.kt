package com.example.playlistmaker.library.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.favorites.domain.models.PlaylistState
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor
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
}