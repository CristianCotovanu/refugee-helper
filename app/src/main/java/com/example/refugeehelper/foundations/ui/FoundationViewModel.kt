package com.example.refugeehelper.foundations.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoundationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "There are no available foundations to be displayed..."
    }
    val text: LiveData<String> = _text
}