package com.fadhly.gestura

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TranslateRepository(private val apiService: ApiService) {

    private val TAG = "VideoRepository"

    fun uploadVideo(videoFile: File): LiveData<Result<TranslateResponse>> =
        liveData(Dispatchers.IO) {
            Log.d(TAG, "uploadVideo: Repository called to upload video file: ${videoFile.absolutePath}")
            emit(Result.Loading)
            try {
                val requestBody = videoFile.asRequestBody("video/mp4".toMediaType())
                val multipart = MultipartBody.Part.createFormData("video", videoFile.name, requestBody)
                val response = apiService.uploadVideo(multipart)

                Log.d(TAG, "uploadVideo: Server response received: $response")
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
                Log.d(TAG, "Error")
            }
        }

    companion object {
        @Volatile
        private var instance: TranslateRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): TranslateRepository =
            instance ?: synchronized(this) {
                instance ?: TranslateRepository(apiService)
            }.also { instance = it }
    }
}
