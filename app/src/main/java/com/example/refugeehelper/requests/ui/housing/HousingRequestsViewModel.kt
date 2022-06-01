package com.example.refugeehelper.requests.ui.housing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HousingRequestsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is housing requests Fragment"
    }
    val text: LiveData<String> = _text
}