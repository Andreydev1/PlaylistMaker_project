package com.example.playlistmaker.library.favorites.data.favorites

import com.example.playlistmaker.library.favorites.data.AppDataBase
import com.example.playlistmaker.library.favorites.data.entity.TrackEntity
import com.example.playlistmaker.library.favorites.domain.db.FavoritesRepository
import com.example.playlistmaker.search.data.DataBaseTrackConvertor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDataBase,
    private val trackDbConvertor: DataBaseTrackConvertor,
) : FavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun saveTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override suspend fun getFavoriteTracksIds(): Flow<List<Long>> = flow {
        emit(appDatabase.trackDao().getTracksIds())
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}