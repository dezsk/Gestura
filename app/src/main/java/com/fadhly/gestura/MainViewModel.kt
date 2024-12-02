package com.fadhly.gestura

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class MainViewModel(private val repository: TranslateRepository): ViewModel() {

    private val TAG = "VideoViewModel"

    private val _uploadResult = MediatorLiveData<Result<TranslateResponse>>()
    val uploadResult: LiveData<Result<TranslateResponse>> get() = _uploadResult

    fun uploadVideo(videoFile: File) {
        Log.d(TAG, "uploadVideo: Uploading video from ViewModel")
        Log.d(TAG, "uploadVideo: Starting upload for video file: ${videoFile.name}")
        val liveData = repository.uploadVideo(videoFile)
        _uploadResult.addSource(liveData) {
            _uploadResult.value = it
        }
    }
}