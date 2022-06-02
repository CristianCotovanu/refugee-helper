package com.example.refugeehelper.requests.ui.housing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HousingRequestsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "There are no housing requests to be displayed..."
    }
    val text: LiveData<String> = _text
}