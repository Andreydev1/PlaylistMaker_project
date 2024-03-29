package com.example.playlistmaker.library.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.favorites.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlist_table SET size=(SELECT COUNT(track_id) FROM track_playlist_table WHERE playlist_id=:playlistId) WHERE id=:playlistId")
    suspend fun updatePlaylistSize(playlistId: Long)

    @Query("UPDATE playlist_table SET name=:name, description=:description, cover_path=:coverPath WHERE id=:playlistId")
    suspend fun updatePlaylistInfo(
        playlistId: Long,
        name: String,
        description: String,
        coverPath: String
    )

    @Query("SELECT * FROM playlist_table ORDER BY creation_time DESC")
    fun getPlaylist(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id=:playlistId")
    suspend fun getPlaylistInfo(playlistId: Long): PlaylistEntity
}