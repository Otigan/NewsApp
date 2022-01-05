package com.example.newsapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.use_case.GetSelectedCountryUseCase
import com.example.newsapp.domain.use_case.GetSelectedLanguageUseCase
import com.example.newsapp.domain.use_case.SelectCountryUseCase
import com.example.newsapp.domain.use_case.SelectLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase,
    private val selectCountryUseCase: SelectCountryUseCase,
    private val getSelectedLanguageUseCase: GetSelectedLanguageUseCase,
    private val selectLanguageUseCase: SelectLanguageUseCase
) : ViewModel() {

    private val _selectedCountry = MutableStateFlow("")
    val selectedCountry = _selectedCountry.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getSelectedCountryUseCase().collectLatest { selectedCountry ->
                    Log.d("SettingsViewModel", "countryViewModel: $selectedCountry ")
                    _selectedCountry.value = selectedCountry
                }
            }
            launch {
                getSelectedLanguageUseCase().collectLatest { language ->
                    Log.d("SettingsViewModel", "languageViewModel: $language ")
                    _selectedLanguage.value = language
                }
            }
        }
    }

    fun setLanguage(language: String) = viewModelScope.launch(Dispatchers.IO) {
        selectLanguageUseCase(language)
    }

    fun setCountry(country: String) = viewModelScope.launch(Dispatchers.IO) {
        selectCountryUseCase(country)
    }


}