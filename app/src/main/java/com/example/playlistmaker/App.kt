package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFS = "playlist_maker_prefs"
const val DARK_THEME_PREFS = "dark_theme_prefs"
class App: Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_PREFS, false)
        darkThemeSwitch(darkTheme)
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
}