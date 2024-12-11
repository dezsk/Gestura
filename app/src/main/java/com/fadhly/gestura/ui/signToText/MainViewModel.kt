package com.fadhly.gestura.ui.signToText

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.fadhly.gestura.data.Result
import com.fadhly.gestura.data.TranslateRepository
import com.fadhly.gestura.data.retrofit.response.TranslateResponse
import com.fadhly.gestura.data.retrofit.response.TranslatedResponse
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val repository: TranslateRepository) : ViewModel() {

    private val TAG = "VideoViewModel"
    lateinit var currentVideoFile: File
    private val _uploadResult = MediatorLiveData<Result<TranslatedResponse>>()
    val uploadResult: LiveData<Result<TranslatedResponse>> get() = _uploadResult

    fun uploadVideo(videoFile: File) {
        Log.d(TAG, "uploadVideo: Uploading video from ViewModel")
        Log.d(TAG, "uploadVideo: Starting upload for video file: ${videoFile.name}")
        val liveData = repository.uploadVideo(videoFile)
        _uploadResult.addSource(liveData) {
            _uploadResult.value = it
        }
    }

    fun createVideoFile(storageDir: File?) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        currentVideoFile = File.createTempFile("VIDEO_${timeStamp}_", ".mp4", storageDir)
    }
}