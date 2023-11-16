package com.example.playlistmaker.library.favorites.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.favorites.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.favorites.data.db.dao.TrackDao
import com.example.playlistmaker.library.favorites.data.db.dao.TrackLibraryDao
import com.example.playlistmaker.library.favorites.data.db.dao.TrackPlaylistDao
import com.example.playlistmaker.library.favorites.data.entity.PlaylistEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackLibraryEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackPlaylistEntity

@Database(
    version = 1, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TrackLibraryEntity::class,
        TrackPlaylistEntity::class
    ]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackLibraryDao(): TrackLibraryDao
    abstract fun trackPlaylistDao(): TrackPlaylistDao
}