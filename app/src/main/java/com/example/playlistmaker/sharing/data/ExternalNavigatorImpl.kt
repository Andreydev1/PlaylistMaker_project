package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {
    override fun navigateToShare(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        val chooserIntent = Intent.createChooser(intent, "Share with:")
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(chooserIntent)
    }

    override fun navigateToOpenLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(termsLink)
        val chooserIntent = Intent.createChooser(intent, "Open in:")
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(chooserIntent)
    }

    override fun navigateToEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}

