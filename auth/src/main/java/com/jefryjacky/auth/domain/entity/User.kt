package com.jefryjacky.auth.domain.entity

data class User (
    val userId:Long,
    val email:String,
    val displayName: String = ""
)