package com.example.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_PREFS
import com.example.playlistmaker.PLAYLIST_MAKER_PREFS
import com.example.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_back)
        val shareBtn = findViewById<Button>(R.id.share_button)
        val supportBtn = findViewById<Button>(R.id.tech_support_button)
        val userAgreementBtn = findViewById<Button>(R.id.user_agreement_button)
        val darkThemeSwitcher = findViewById<SwitchCompat>(R.id.dark_theme_switcher)
        backBtn.setOnClickListener {
            finish()
        }
        shareBtn.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
                startActivity(Intent.createChooser(this, "Share with:"))
            }
        }
        supportBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.share_app_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
                startActivity(this)
            }
        }
        userAgreementBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.user_agreement_link))
                )
            )
        }
        darkThemeSwitcher.setOnCheckedChangeListener{switcher, checked->
            (applicationContext as App).darkThemeSwitch(checked)
            val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_PREFS, checked)
                .apply()
        }
    }
}


