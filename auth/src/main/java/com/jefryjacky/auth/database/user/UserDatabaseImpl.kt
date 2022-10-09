package com.jefryjacky.auth.database.user

import com.jefryjacky.auth.database.user.entity.UserTokenDb
import com.jefryjacky.auth.domain.entity.UserToken
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import javax.inject.Inject

class UserDatabaseImpl @Inject constructor(
    private val userDao: UserDao
): UserDatabase {
    override fun saveToken(token: UserToken) {
        val userTokenDb = UserTokenDb(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            expiredDate = token.expiredDate
        )
        userDao.saveUserToken(userTokenDb)
    }

    override fun getToken(): UserToken? {
        return userDao.getUserToken()?.toUserToken()
    }
}