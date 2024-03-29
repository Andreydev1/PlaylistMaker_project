package com.example.playlistmaker.search.data.localstorage

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.LocalStorage
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchHistory
import com.example.playlistmaker.settings.domain.models.Theme
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.utils.SEARCH_HISTORY
import com.example.playlistmaker.utils.THEME_SWITCHER
import com.google.gson.Gson

class LocalStorageImpl(private val sharedPrefs: SharedPreferences, private val gson: Gson):
    LocalStorage {

    override fun removeTrackHistory() {
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

    override fun getTrackHistory(): Array<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null) ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    override fun updateTrackHistory(tracksList: MutableList<Track>) {
        if (tracksList.size > SearchHistory.HISTORY_SIZE)
            tracksList.removeAt(SearchHistory.HISTORY_SIZE)

        val json = gson.toJson(tracksList)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    override fun getThemeSettings(): ThemeSettings {
        return when (sharedPrefs.getBoolean(THEME_SWITCHER, false)) {
            true -> ThemeSettings(Theme.DARK)
            else -> ThemeSettings(Theme.LIGHT)
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPrefs.edit()
            .putBoolean(THEME_SWITCHER, settings.theme == Theme.DARK)
            .apply()
    }

}