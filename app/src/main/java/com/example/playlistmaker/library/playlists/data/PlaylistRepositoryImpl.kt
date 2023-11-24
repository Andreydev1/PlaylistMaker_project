package com.example.playlistmaker.library.playlists.data

import com.example.playlistmaker.library.favorites.data.AppDataBase
import com.example.playlistmaker.library.favorites.data.entity.PlaylistEntity
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.domain.db.PlaylistRepository
import com.example.playlistmaker.search.data.PlaylistDbConvertor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDataBase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromTrackEntity(playlists))
    }

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        if (playlist.id != null) {
            appDatabase.trackLibraryDao().insertTrack(playlistDbConvertor.map(track))
            appDatabase.trackPlaylistDao()
                .insertRelationship(playlistDbConvertor.map(track.id, playlist.id))
            appDatabase.playlistDao().updatePlaylistSize(playlist.id, playlist.size + 1)
            return true
        } else {
            return false
        }
    }

    override suspend fun getPlaylistsIdsContainTrack(track: Track): List<Long> {
        return appDatabase.trackPlaylistDao().getPlaylistsIdsContainTrack(track.id)
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}