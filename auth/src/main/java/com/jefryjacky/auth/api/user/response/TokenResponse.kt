package com.jefryjacky.auth.api.user.response

import com.google.gson.annotations.SerializedName
import com.jefryjacky.auth.domain.entity.UserToken

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken:String,
    @SerializedName("refresh_token")
    val refreshToken:String,
    @SerializedName("expired_date")
    val expiredDate:Long
){
    fun toUserToken(): UserToken {
        return UserToken(accessToken, refreshToken, expiredDate)
    }
}
