package com.fadhly.gestura

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload-video")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part
    ): TranslateResponse
}