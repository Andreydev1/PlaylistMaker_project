package com.example.playlistmaker.library.playlists.createdPlaylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.createdPlaylist.domain.models.CreatedPlaylistState
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreatedPlaylistViewModel(private val playlistsInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<CreatedPlaylistState>()
    fun observeState(): LiveData<CreatedPlaylistState> = stateLiveData

    fun getPlaylistInfo(playlistId: Long) {
        viewModelScope.launch {
            val playlist = playlistsInteractor.getPlaylistInfo(playlistId)
            val trackList = playlistsInteractor.getTracksInPlaylist(playlistId)
            stateLiveData.postValue(CreatedPlaylistState.Success(playlist, trackList))
        }
    }

    fun deleteTrack(track: Track, playlistId: Long) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrack(track.id, playlistId)
            val playlistList = playlistsInteractor.getPlaylistsIdsContainTrack(track)
            if (playlistList.isEmpty())
                playlistsInteractor.deleteTrackFromLibrary(track)
            getPlaylistInfo(playlistId)
        }
    }

    fun sharePlaylist(playlist: Playlist, trackList: List<Track>) {
        if (trackList.isEmpty())
            stateLiveData.postValue(CreatedPlaylistState.Empty)
        else {
            val intent = playlistsInteractor.sharePlaylist(getSharingText(playlist, trackList))
            stateLiveData.postValue(CreatedPlaylistState.Share(intent))
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlist)
            stateLiveData.postValue(CreatedPlaylistState.Deleted)
        }
    }

    private fun getSharingText(playlist: Playlist, trackList: List<Track>): String {
        var sharingText = "${playlist.name}\n${playlist.description}\n${playlist.size} трека\n"
        for (i in trackList.indices) {
            sharingText += "${i + 1}. ${trackList[i].artistName} - ${trackList[i].trackName} (${trackList[i].getTrackTimeResulted()})\n"
        }
        return sharingText
    }

    fun countPlaylistDuration(trackList: List<Track>): String {
        var durationSum = 0
        for (track in trackList) {
            durationSum += (track.trackTime?.toInt() ?: 0)
        }

        return SimpleDateFormat("mm", Locale.getDefault()).format(durationSum)
    }
}