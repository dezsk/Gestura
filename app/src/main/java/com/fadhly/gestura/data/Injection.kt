package com.fadhly.gestura.data

import android.content.Context
import com.fadhly.gestura.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): TranslateRepository {
        val apiService = ApiConfig.apiService()
        return TranslateRepository.getInstance(apiService)
    }
}