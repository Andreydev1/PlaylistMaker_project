package com.example.playlistmaker.library.playlists.domain.impl

import android.content.Intent
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.playlists.domain.db.PlaylistRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.data.ExternalNavigator
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistsRepository: PlaylistRepository,
    private val externalNavigator: ExternalNavigator
) :
    PlaylistInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistsRepository.addTrackInPlaylist(track, playlist)
    }

    override suspend fun getPlaylistsIdsContainTrack(track: Track): List<Long> {
        return playlistsRepository.getPlaylistsIdsContainTrack(track)
    }

    override suspend fun getPlaylistInfo(playlistId: Long): Playlist {
        return playlistsRepository.getPlaylistInfo(playlistId)
    }

    override suspend fun getTracksInPlaylist(playlistId: Long): List<Track> {
        return playlistsRepository.getTracksInPlaylist(playlistId)
    }

    override suspend fun deleteTrack(trackId: Long, playlistId: Long) {
        return playlistsRepository.deleteTrack(trackId, playlistId)
    }

    override suspend fun deleteTrackFromLibrary(track: Track) {
        return playlistsRepository.deleteTrackFromLibrary(track)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        return playlistsRepository.deletePlaylist(playlist)
    }

    override fun sharePlaylist(playlist: String): Intent {
        return externalNavigator.sharePlaylist(playlist)
    }

    override suspend fun updatePlaylist(
        playlistId: Long,
        name: String,
        description: String,
        coverPath: String
    ) {
        return playlistsRepository.updatePlaylist(playlistId, name, description, coverPath)
    }
}