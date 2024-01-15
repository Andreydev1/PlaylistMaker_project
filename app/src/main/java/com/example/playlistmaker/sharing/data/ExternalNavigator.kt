package com.example.playlistmaker.sharing.data


import android.content.Intent
import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun sharePlaylist(playlist: String): Intent
    fun navigateToShare(shareAppLink: String)
    fun navigateToOpenLink(termsLink: String)
    fun navigateToEmail(supportEmailData: EmailData)
}