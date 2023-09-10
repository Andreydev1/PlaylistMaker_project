package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.model.EmailData

interface ExternalNavigator {
    fun navigateToShare(shareAppLink: String)
    fun navigateToOpenLink(termsLink: String)
    fun navigateToEmail(supportEmailData: EmailData)
}