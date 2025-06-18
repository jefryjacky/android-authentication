package com.jefryjacky.auth.domain.entity

data class User (
    val userId:Long = 0,
    val email:String = "",
    val displayName: String = ""
)