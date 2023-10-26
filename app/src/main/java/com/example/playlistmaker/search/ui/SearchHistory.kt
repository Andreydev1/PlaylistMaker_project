package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.data.LocalStorage
import com.example.playlistmaker.search.domain.Track

class SearchHistory(private val localStorage: LocalStorage) {
    var tracksList: MutableList<Track> = get().toMutableList()

    fun get(): Array<Track> {
        return localStorage.getTrackHistory()
    }

    fun add(track: Track) {
        tracksList.remove(track)
        tracksList.add(0, track)

        if (tracksList.size > HISTORY_SIZE)
            tracksList.removeAt(HISTORY_SIZE)

        localStorage.updateTrackHistory(tracksList)
    }

    fun clear() {
        tracksList.clear()
        localStorage.removeTrackHistory()
    }

    companion object {
        const val HISTORY_SIZE = 10
    }
}