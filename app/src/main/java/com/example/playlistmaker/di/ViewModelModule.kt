package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.view_models.FavoritesViewModel
import com.example.playlistmaker.library.ui.view_models.PlaylistsViewModel
import com.example.playlistmaker.player.view_model.PlayerViewModel
import com.example.playlistmaker.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get(), get())
    }

    viewModel {
        TracksSearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        PlaylistsViewModel()
    }
}