package com.example.playlistmaker.search.data

import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.search.domain.Track

interface LocalStorage {
    fun removeTrackHistory()
    fun getTrackHistory(): Array<Track>
    fun updateTrackHistory(tracksList: MutableList<Track>)
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}