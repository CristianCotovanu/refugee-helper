package com.example.refugeehelper.requests.ui.work

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkRequestsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is work requests Fragment"
    }
    val text: LiveData<String> = _text
}