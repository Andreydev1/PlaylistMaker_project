package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.settings.domain.models.ThemeSettings

interface LocalStorage {
    fun removeTrackHistory()
    fun getTrackHistory(): Array<Track>
    fun updateTrackHistory(tracksList: MutableList<Track>)
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}