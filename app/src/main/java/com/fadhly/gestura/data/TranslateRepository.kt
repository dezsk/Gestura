package com.fadhly.gestura.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fadhly.gestura.data.retrofit.response.TranslateResponse
import com.fadhly.gestura.data.retrofit.ApiService
import com.fadhly.gestura.data.retrofit.response.ImageResponse
import com.fadhly.gestura.data.retrofit.response.TranslatedResponse
import com.fadhly.gestura.data.retrofit.response.VideoResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class TranslateRepository(private val apiService: ApiService, private val appContext: Context) {

    private val TAG = "VideoRepository"

    fun uploadVideo(videoFile: File): LiveData<Result<TranslatedResponse>> =
        liveData(Dispatchers.IO) {
            Log.d(TAG, "uploadVideo: Repository called to upload video file: ${videoFile.absolutePath}")
            emit(Result.Loading)
            try {
                val requestBody = videoFile.asRequestBody("video/mp4".toMediaType())
                val multipart = MultipartBody.Part.createFormData("file", videoFile.name, requestBody)
                val response = apiService.uploadVideo(multipart)

                Log.d(TAG, "uploadVideo: Server response received: $response")
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
                Log.d(TAG, "Error")
            }
        }

    fun uploadText(text: String): LiveData<Result<File>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.uploadText(text)

                // Save file locally
                val file = File(appContext.cacheDir, "translated_video.mp4")
                FileOutputStream(file).use { output ->
                    response.byteStream().copyTo(output)
                }

                emit(Result.Success(file))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: TranslateRepository? = null
        fun getInstance(
            apiService: ApiService,
            appContext: Context
        ): TranslateRepository =
            instance ?: synchronized(this) {
                instance ?: TranslateRepository(apiService, appContext)
            }.also { instance = it }
    }
}
