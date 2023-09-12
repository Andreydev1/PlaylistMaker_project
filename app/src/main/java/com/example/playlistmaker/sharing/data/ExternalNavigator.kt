package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun navigateToShare(shareAppLink: String)
    fun navigateToOpenLink(termsLink: String)
    fun navigateToEmail(supportEmailData: EmailData)
}