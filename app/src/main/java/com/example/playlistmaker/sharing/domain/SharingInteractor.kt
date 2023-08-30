package com.example.playlistmaker.sharing.domain

import android.content.Intent
import com.example.playlistmaker.sharing.model.EmailData


interface SharingInteractor {
    fun shareApp(navigate: (String) -> Unit)
    fun openTerms(navigate: (String) -> Unit)
    fun openSupport(navigate: (EmailData) -> Unit)
}