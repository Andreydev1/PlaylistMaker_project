package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.SearchHistory.Companion.SEARCH_HISTORY
import com.example.playlistmaker.domain.TrackStorage
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class TrackStorageImpl(val sharedPreferences: SharedPreferences, val gson: Gson): TrackStorage {
    override fun removeTrackHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

    override fun getTrackHistory(): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    override fun updateTrackHistory(tracksList: MutableList<Track>) {
        if (tracksList.size > SearchHistory.SEARCH_HISTORY_SIZE)
            tracksList.removeAt(SearchHistory.SEARCH_HISTORY_SIZE)

        val json = gson.toJson(tracksList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }
}