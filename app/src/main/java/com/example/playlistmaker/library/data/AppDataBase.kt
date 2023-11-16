package com.example.playlistmaker.library.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}