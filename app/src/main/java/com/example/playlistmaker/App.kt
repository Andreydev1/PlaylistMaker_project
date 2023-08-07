package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.TrackStorageImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.TrackStorage
import com.google.gson.Gson

const val PLAYLIST_MAKER_PREFS = "playlist_maker_prefs"
const val DARK_THEME_PREFS = "dark_theme_prefs"
private lateinit var trackRepository:TrackRepositoryImpl
private lateinit var trackStorage:TrackStorageImpl
class App: Application() {
    private var gson = Gson()
    companion object{
        lateinit var instance:App
        private set
    }

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        instance = this



        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_PREFS, false)
        darkThemeSwitch(darkTheme)

        trackStorage = TrackStorageImpl(sharedPrefs, gson)
        trackRepository = TrackRepositoryImpl()
    }

    fun darkThemeSwitch(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            }
        else AppCompatDelegate.MODE_NIGHT_NO
        )

    }
    fun getTracksStorage():TrackStorage{
        return trackStorage
    }
    fun getTrackRepository():TrackRepository{
        return trackRepository
    }
}