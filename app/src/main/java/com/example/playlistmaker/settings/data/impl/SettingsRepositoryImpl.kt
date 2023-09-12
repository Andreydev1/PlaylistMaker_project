package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.search.data.LocalStorage
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.data.api.SettingsRepository

class SettingsRepositoryImpl(private val localStorage: LocalStorage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return localStorage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        localStorage.updateThemeSetting(settings)
    }
}