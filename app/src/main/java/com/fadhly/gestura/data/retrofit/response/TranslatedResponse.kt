package com.fadhly.gestura.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class TranslatedResponse(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("hasil_prediksi")
	val hasilPrediksi: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
