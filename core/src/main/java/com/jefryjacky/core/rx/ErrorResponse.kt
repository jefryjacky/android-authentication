package com.jefryjacky.core.rx

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String
)
