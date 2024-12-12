package com.fadhly.gestura.ui.textToSign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.fadhly.gestura.data.Result
import com.fadhly.gestura.data.TranslateRepository
import com.fadhly.gestura.data.retrofit.response.VideoResponse
import java.io.File

class TextToSignViewModel(private val repository: TranslateRepository): ViewModel() {
    private val _videoResponse = MediatorLiveData<Result<File>>()
    val videoResponse: LiveData<Result<File>> = _videoResponse

    fun uploadText(text: String) {
        val liveData = repository.uploadText(text)
        _videoResponse.addSource(liveData) {
            _videoResponse.value = it
        }
    }
}