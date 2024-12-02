package com.fadhly.gestura

import android.content.Context

object Injection {
    fun provideRepository(context: Context): TranslateRepository{
        val apiService = ApiConfig.apiService()
        return TranslateRepository.getInstance(apiService)
    }
}