package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings

class SettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

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

        binding.shareButton.setOnClickListener {
            startActivity(viewModel.shareApp())
        }

        binding.shareButton.setOnClickListener {
            startActivity(viewModel.openSupport())
        }

        binding.userAgreementButton.setOnClickListener {
            startActivity(viewModel.openTerms())
        }

        binding.darkThemeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.updateThemeSetting(getThemeSettings(checked))
        }
    }

    private fun getThemeSettings(checked: Boolean): ThemeSettings {
        return ThemeSettings(when (checked) {
            true -> Theme.DARK
            else -> Theme.LIGHT
        })
    }

}