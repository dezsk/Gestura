package com.fadhly.gestura

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadVideo(
        @Part video: MultipartBody.Part
    ): TranslateResponse
}