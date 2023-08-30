package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

        private val navigator: ExternalNavigatorImpl = ExternalNavigatorImpl(application)
        private val sharingInteractor = Creator.provideSharingInteractor(application, navigator)
        private val settingsInteractor = Creator.provideSettingsInteractor(getApplication<Application>())


        companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    fun shareApp(navigator: ExternalNavigator) {
        sharingInteractor.shareApp {
            navigator.navigateToShare(it)
        }
    }

    fun openTerms(navigator: ExternalNavigator) {
        sharingInteractor.openTerms {
            navigator.navigateToOpenLink(it)
        }
    }

    fun openSupport(navigator: ExternalNavigator) {
        sharingInteractor.openSupport {
            navigator.navigateToEmail(it)
        }
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
}