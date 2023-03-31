package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_back)

        backBtn.setOnClickListener {
            finish()
        }

        val shareBtn = findViewById<Button>(R.id.share_button)
        shareBtn.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
                startActivity(Intent.createChooser(this, "Share with:"))
            }
        }

        val supportBtn = findViewById<Button>(R.id.tech_support_button)
        supportBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.share_app_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
                startActivity(this)
            }
        }

        val userAgreementBtn = findViewById<Button>(R.id.user_agreement_button)
        userAgreementBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.user_agreement_link))
                )
            )
        }

    }
}


