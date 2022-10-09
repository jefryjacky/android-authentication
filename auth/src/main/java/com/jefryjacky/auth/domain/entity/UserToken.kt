package com.jefryjacky.auth.domain.entity

data class UserToken(
    val accessToken:String,
    val refreshToken:String,
    val expiredDate:Long
)