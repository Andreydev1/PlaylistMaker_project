package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }
    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}