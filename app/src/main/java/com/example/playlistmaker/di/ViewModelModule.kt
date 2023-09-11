package com.example.playlistmaker.di

import com.example.playlistmaker.player.view_model.PlayerViewModel
import com.example.playlistmaker.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        TracksSearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}