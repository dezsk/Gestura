package com.fadhly.gestura.ui.textToSign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.fadhly.gestura.data.Result
import com.fadhly.gestura.data.TranslateRepository
import com.fadhly.gestura.data.retrofit.response.ImageResponse

class TextToSignViewModel(private val repository: TranslateRepository): ViewModel() {
    private val _imageResponse = MediatorLiveData<Result<ImageResponse>>()
    val imageResponse: LiveData<Result<ImageResponse>> = _imageResponse

    fun uploadText(text: String) {
        val liveData = repository.uploadText(text)
        _imageResponse.addSource(liveData) {
            _imageResponse.value = it
        }
    }
}