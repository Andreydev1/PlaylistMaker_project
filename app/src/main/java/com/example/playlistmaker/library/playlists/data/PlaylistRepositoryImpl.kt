package com.example.playlistmaker.library.playlists.data

import com.example.playlistmaker.library.favorites.data.AppDataBase
import com.example.playlistmaker.library.favorites.data.entity.PlaylistEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackLibraryEntity
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.domain.db.PlaylistRepository
import com.example.playlistmaker.search.data.DataBasePlaylistConvertor
import com.example.playlistmaker.search.data.DataBaseTrackConvertor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDataBase,
    private val dataBasePlaylistConvertor: DataBasePlaylistConvertor,
    private val trackDbConvertor: DataBaseTrackConvertor
) : PlaylistRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylist().map { convertFromPlaylistEntity(it) }

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        if (playlist.id != null) {
            appDatabase.trackLibraryDao().insertTrack(dataBasePlaylistConvertor.map(track))
            appDatabase.trackPlaylistDao()
                .insertRelationship(dataBasePlaylistConvertor.map(track.id, playlist.id))
            appDatabase.playlistDao().updatePlaylistSize(playlist.id)
            return true
        } else {
            return false
        }
    }

    override suspend fun getPlaylistsIdsContainTrack(track: Track): List<Long> {
        return appDatabase.trackPlaylistDao().getPlaylistsIdsContainTrack(track.id)
    }

    private fun convertFromTrackLibraryEntity(trackList: List<TrackLibraryEntity>): List<Track> {
        return trackList.map { track -> trackDbConvertor.map(track) }
    }

    override suspend fun getPlaylistInfo(playlistId: Long): Playlist {
        return dataBasePlaylistConvertor.map(appDatabase.playlistDao().getPlaylistInfo(playlistId))
    }

    override suspend fun getTracksInPlaylist(playlistId: Long): List<Track> {
        val trackList = appDatabase.trackLibraryDao().getTracksInPlaylist(playlistId)
        return convertFromTrackLibraryEntity(trackList)
    }

    override suspend fun deleteTrack(trackId: Long, playlistId: Long) {
        appDatabase.trackPlaylistDao()
            .deleteRelationship(dataBasePlaylistConvertor.map(trackId, playlistId))
        appDatabase.playlistDao().updatePlaylistSize(playlistId)
    }

    override suspend fun deleteTrackFromLibrary(track: Track) {
        return appDatabase.trackLibraryDao().deleteTrack(dataBasePlaylistConvertor.map(track))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlist.id?.let { appDatabase.trackPlaylistDao().deletePlaylistRelationships(it) }
        appDatabase.trackLibraryDao().deleteUnusedTracks()
        appDatabase.playlistDao().deletePlaylist(dataBasePlaylistConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(
        playlistId: Long,
        name: String,
        description: String,
        coverPath: String
    ) {
        appDatabase.playlistDao().updatePlaylistInfo(playlistId, name, description, coverPath)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> dataBasePlaylistConvertor.map(playlist) }
    }
}