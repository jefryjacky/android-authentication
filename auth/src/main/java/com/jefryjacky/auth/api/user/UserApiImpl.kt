package com.jefryjacky.auth.api.user

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.entity.UserToken
import com.jefryjacky.auth.domain.repository.api.UserApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserApiImpl @Inject constructor(
    private val userService: UserService
): UserApi {
    override fun login(email: String, password: String): Single<UserToken> {
        return userService.login(email, password).map {
            it.toUserToken()
        }
    }

    override fun refreshToken(refreshToken: String): Single<UserToken> {
        return userService.refreshToken("refresh_token", refreshToken).map {
            it.toUserToken()
        }
    }

    override fun loginGoogle(token: String): Single<UserToken> {
        return userService.loginGoogle(token).map {
            it.toUserToken()
        }
    }

    override fun register(email: String, password: String): Completable {
        return userService.register(email, password)
    }

    override fun getUser(): Single<User> {
        return userService.getUser().map {
            it.toUser()
        }
    }

    override fun requestEmailVerification(email:String):Completable {
        return userService.requestEmailVerification(email)
    }

    override fun verifyEmail(token: String): Single<UserToken> {
        return userService.verifyEmail(token).map {
            it.toUserToken()
        }
    }

    override fun forgotPassword(email: String): Completable {
        return userService.forgotPassword(email)
    }

    override fun updatePasswordByToken(newPassword: String, token: String): Completable {
        return userService.updatePasswordByToken(
            newPassword, token
        )
    }

    override fun updatePassowrd(password: String, newPassword: String): Completable {
        return userService.updatePassword(password, newPassword)
    }
}