package com.jefryjacky.auth.domain.repository.api

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.entity.UserToken
import io.reactivex.Completable
import io.reactivex.Single

interface UserApi {
    fun login(email:String, password:String):Single<UserToken>
    fun refreshToken(refreshToken:String):Single<UserToken>
    fun loginGoogle(token:String):Single<UserToken>
    fun register(email:String, password: String):Completable
    fun getUser():Single<User>
    fun requestEmailVerification(email:String):Completable
    fun verifyEmail(token: String):Single<UserToken>
    fun forgotPassword(email:String):Completable
    fun updatePasswordByToken(newPassword:String, token: String):Completable
    fun updatePassowrd(password:String, newPassword: String):Completable
    fun requestEmailVerificationOtp(email:String):Completable
    fun verifyEmailOtp(email:String, otp:String): Single<UserToken>
    fun requestChangePasswordOtp(email:String):Completable
    fun updatePasswordByOtp(email:String, otp:String):Completable
}