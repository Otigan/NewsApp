package com.example.newsapp.features.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.newsapp.R
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)

        val pref = findPreference<ListPreference>("country")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val country = sharedPreferences.getString("country", null)

        val arr = resources.getStringArray(R.array.pref_country_values)

        if (country == null) {
            val currCountry = arr.indexOf(Locale.getDefault().country.toLowerCase())
            Toast.makeText(context, currCountry.toString(), Toast.LENGTH_SHORT).show()
            pref?.setValueIndex(currCountry)
        }
    }
}