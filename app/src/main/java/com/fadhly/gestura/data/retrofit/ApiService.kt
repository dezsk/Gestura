package com.fadhly.gestura.data.retrofit

import com.fadhly.gestura.data.retrofit.response.ImageResponse
import com.fadhly.gestura.data.retrofit.response.TranslateResponse
import com.fadhly.gestura.data.retrofit.response.TranslatedResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload-video")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part
    ): TranslatedResponse

    @POST("upload-text")
    suspend fun uploadText(
        @Field("text") text: String
    ): ImageResponse
}