package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.domain.models.Theme
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        binding.darkThemeSwitcher.isChecked = viewModel.isNightModeChecked()
    }

    private fun setListeners() {

        binding.techSupportButton.setOnClickListener {
            viewModel.openSupport()
        }
        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }
        binding.userAgreementButton.setOnClickListener {
            viewModel.openTerms()
        }
        binding.darkThemeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (requireContext().applicationContext as App).switchTheme(checked)
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
