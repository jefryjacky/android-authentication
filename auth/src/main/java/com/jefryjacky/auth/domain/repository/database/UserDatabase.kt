package com.jefryjacky.auth.domain.repository.database

import com.jefryjacky.auth.domain.entity.UserToken

interface UserDatabase {
    fun saveToken(token: UserToken)
    fun getToken():UserToken?
}