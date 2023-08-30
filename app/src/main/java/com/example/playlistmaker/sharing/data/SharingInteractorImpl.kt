package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.model.EmailData

class SharingInteractorImpl(
    private val navigator: ExternalNavigator,
    private val sharingRepository: SharingRepository,
    private val context: Context
) : SharingInteractor {
    override fun shareApp(navigate: (String) -> Unit) {
        val shareLink = sharingRepository.getShareAppLink()
        navigate.invoke(shareLink)
    }

    override fun openTerms(navigate: (String) -> Unit) {
        val termsLink = sharingRepository.getTermsLink()
        navigate.invoke(termsLink)
    }

    override fun openSupport(navigate: (EmailData) -> Unit) {
        val emailData = EmailData(
            email  = context.getString(R.string.share_app_email),
            subject = context.getString(R.string.share_app_subject),
            text = context.getString(R.string.share_app_message)
        )
        navigator.navigateToEmail(emailData)
    }
}

