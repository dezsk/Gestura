package com.fadhly.gestura

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TranslateResponse(

    @field:SerializedName("translateResult")
    val translateResult: TranslateResult? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class TranslateResult(

    @field:SerializedName("text")
    val text: String? = null,

)
