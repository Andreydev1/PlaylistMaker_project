package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.domain.models.ThemeSettings

class SettingsInteractorImpl(val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }
    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}