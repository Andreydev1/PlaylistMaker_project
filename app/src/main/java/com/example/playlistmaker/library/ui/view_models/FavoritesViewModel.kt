package com.example.playlistmaker.library.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.models.FavoritesState
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun getFavoritesList() {
        viewModelScope.launch {
            favoritesInteractor
                .favoritesTracks()
                .collect { list ->
                    val favoritesList = mutableListOf<Track>()
                    favoritesList.addAll(list)
                    if (favoritesList.isNotEmpty()) {
                        stateLiveData.postValue(FavoritesState.Success(favoritesList))
                    } else {
                        stateLiveData.postValue(FavoritesState.Empty)
                    }
                }
        }
    }
}