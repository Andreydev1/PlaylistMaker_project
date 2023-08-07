package com.example.playlistmaker


import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPrefs: SharedPreferences) {


    var trackList: MutableList<Track> = get().toMutableList()

    private fun get(): Array<Track> {
       val json = sharedPrefs.getString(SEARCH_HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track> ::class.java)
    }

        fun add(track: Track){
            trackList.remove(track)
            trackList.add(0, track)

            if (trackList.size > SEARCH_HISTORY_SIZE) trackList.removeAt(SEARCH_HISTORY_SIZE)

            val json = Gson().toJson(trackList)
            sharedPrefs.edit {
                putString(SEARCH_HISTORY, json)
            }
        }

        fun clear(){
            trackList.clear()
            sharedPrefs.edit()
                .remove(SEARCH_HISTORY)
                .apply()
        }
    companion object{
        const val SEARCH_HISTORY_SIZE = 10
        const val SEARCH_HISTORY = "search_history"
    }
    }
