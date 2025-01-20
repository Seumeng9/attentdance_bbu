package com.bbu.attendancetracking.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QRViewModel : ViewModel() {
    private val _qrContent = MutableLiveData<String>()
    val qrContent: LiveData<String> get() = _qrContent

    // Function to set the content of the QR code
    fun setQRContent(content: String) {
        _qrContent.value = content
    }
}
