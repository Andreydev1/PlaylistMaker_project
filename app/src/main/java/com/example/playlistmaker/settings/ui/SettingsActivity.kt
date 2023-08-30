package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.sharing.model.EmailData

class SettingsActivity : AppCompatActivity(), ExternalNavigator {


    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        binding.darkThemeSwitcher.isChecked = viewModel.isNightModeChecked()
    }

    private fun setListeners() {
        binding.settingsBack.setOnClickListener {
            finish()
        }

        binding.techSupportButton.setOnClickListener {
            try {
                viewModel.openSupport(this)
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Ошибка при запуске активности ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.shareButton.setOnClickListener {
            try {
                viewModel.shareApp(this)
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Ошибка при запуске активности ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.userAgreementButton.setOnClickListener {
            try {
                viewModel.openTerms(this)
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Ошибка при запуске активности ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.darkThemeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.updateThemeSetting(getThemeSettings(checked))
        }
    }

    private fun getThemeSettings(checked: Boolean): ThemeSettings {
        return ThemeSettings(
            when (checked) {
                true -> Theme.DARK
                else -> Theme.LIGHT
            }
        )
    }


    override fun navigateToShare(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        val chooserIntent = Intent.createChooser(intent, "Share with:")
        startActivity(chooserIntent)
    }

    override fun navigateToOpenLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(termsLink)
        val chooserIntent = Intent.createChooser(intent, "Open in:")
        startActivity(chooserIntent)
    }

    override fun navigateToEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        startActivity(intent)
    }
}