package com.example.playlistmaker.library.playlists.newPlaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.playlists.newPlaylist.domain.db.NewPlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val newPlaylistInteractor: NewPlaylistInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    fun createPlaylist(name: String, description: String, coverPath: String) {
        viewModelScope.launch {
            newPlaylistInteractor.savePlaylist(name, description, coverPath)
        }
    }

    fun updatePlaylist(playlistId: Long?, name: String, description: String, coverPath: String) {
        playlistId?.let {
            viewModelScope.launch {
                playlistInteractor.updatePlaylist(it, name, description, coverPath)
            }
        }
    }

    fun getPlaylistInfo(playlistId: Long) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistInfo(playlistId)
            playlistLiveData.postValue(playlist)
        }
    }
}