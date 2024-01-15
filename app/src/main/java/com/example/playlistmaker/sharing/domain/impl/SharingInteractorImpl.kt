package com.example.playlistmaker.sharing.domain.impl


import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val navigator: ExternalNavigator
) : SharingInteractor {

    override fun shareApp() {
        navigator.navigateToShare(getShareLink())
    }

    override fun openTerms() {
        navigator.navigateToOpenLink(getTermsLink())
    }

    override fun openSupport() {
       navigator.navigateToEmail(getEmailData())
    }

    private fun getShareLink():String{
        return APP_LINK
    }

     private fun getEmailData() : EmailData {
        return EmailData(
            email = EMAIL,
            subject = SUBJECT,
            text = TEXT_MESSAGE
        )
    }
    private fun getTermsLink():String{
        return TERMS_LINK
    }
    companion object {
        private const val EMAIL = "S4cr1fic3@yandex.ru"
        private const val SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val TEXT_MESSAGE = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val APP_LINK = "https://practicum.yandex.ru/android-developer/"
        private const val TERMS_LINK = "https://yandex.ru/legal/practicum_offer/"
    }
}


