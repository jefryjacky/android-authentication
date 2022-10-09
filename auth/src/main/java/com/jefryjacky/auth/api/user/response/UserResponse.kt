package com.jefryjacky.auth.api.user.response

import com.google.gson.annotations.SerializedName
import com.jefryjacky.auth.domain.entity.User

data class UserResponse(
    @SerializedName("user_id")
    val userId: Long = 0,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified")
    val emailverified:Boolean
){
    fun toUser():User{
        return User(
            userId,
            email
        )
    }
}