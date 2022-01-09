package com.example.newsapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.newsapp.R
import com.example.newsapp.presentation.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countries = resources.getStringArray(R.array.pref_country_values)
        val countryPref = findPreference<ListPreference>("country")
        val languagePref = findPreference<ListPreference>("language")

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    settingsViewModel.selectedCountry.collectLatest { country ->
                        Log.d("SettingsFragment", "country: $country")
                        countryPref?.value = country
                    }
                }
                launch {
                    settingsViewModel.selectedLanguage.collectLatest { language ->
                        Log.d("SettingsFragment", "language: $language")
                        languagePref?.value = language
                    }
                }
            }
        }

        countryPref?.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                settingsViewModel.setCountry(newValue.toString())
                true
            }
        }
        languagePref?.let {
            it.setOnPreferenceChangeListener { _, newValue ->
                settingsViewModel.setLocale(requireContext(), newValue.toString())
                activity?.recreate()
                true
            }
        }
    }
}