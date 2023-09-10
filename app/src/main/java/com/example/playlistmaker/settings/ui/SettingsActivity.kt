package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                viewModel.openSupport()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.shareButton.setOnClickListener {
            try {
                viewModel.shareApp()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.userAgreementButton.setOnClickListener {
            try {
                viewModel.openTerms()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingsActivity,
                    "${e.message}",
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
}