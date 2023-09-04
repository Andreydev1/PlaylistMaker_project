package com.example.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor


class SettingsViewModel(private val application: App,
                        private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : AndroidViewModel(application) {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    private fun getThemeSettings(): ThemeSettings {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(settings)
    }

    fun isNightModeChecked(): Boolean {
        return getThemeSettings().theme == Theme.DARK
    }
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val sharingInteractor =
                    Creator.provideSharingInteractor(application.applicationContext)
                val settingsInteractor =
                    Creator.provideSettingsInteractor(application.applicationContext)
                SettingsViewModel(application, sharingInteractor, settingsInteractor)
            }
        }
    }
}
