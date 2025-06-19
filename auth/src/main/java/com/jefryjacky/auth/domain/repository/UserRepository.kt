package com.jefryjacky.auth.domain.repository

import com.jefryjacky.auth.domain.entity.User
import com.jefryjacky.auth.domain.entity.UserToken
import com.jefryjacky.core.domain.scheduler.Schedulers
import com.jefryjacky.auth.domain.repository.api.UserApi
import com.jefryjacky.auth.domain.repository.database.UserDatabase
import com.jefryjacky.core.di.IoDispatcher
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDatabase: UserDatabase,
    private val schedulers: Schedulers,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
){

    fun login(email:String, password:String):Single<UserToken>{
        return userApi.login(email, password)
            .doOnSuccess {
                Completable.fromCallable {
                    userDatabase.saveToken(it)
                }.subscribeOn(schedulers.diskThread())
                    .subscribe()
            }
            .subscribeOn(schedulers.netWorkThread())
    }

    fun loginGoogle(token:String):Single<UserToken>{
        return userApi.loginGoogle(token)
            .doOnSuccess {
                Completable.fromCallable {
                    userDatabase.saveToken(it)
                }.subscribeOn(schedulers.diskThread())
                    .subscribe()
            }.subscribeOn(schedulers.netWorkThread())
    }

    fun register(email:String, password:String):Completable{
        return userApi.register(email, password)
          .subscribeOn(schedulers.netWorkThread())
    }

    fun getUser():Single<User>{
        return userApi.getUser()
            .subscribeOn(schedulers.netWorkThread())
    }

    fun requestEmailVerification(email:String):Completable{
        return userApi.requestEmailVerification(email)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun verifyEmail(token:String):Completable{
        return userApi.verifyEmail(token)
            .doOnSuccess {
                Completable.fromCallable {
                   userDatabase.saveToken(it)
                }.subscribeOn(schedulers.diskThread())
                    .subscribe()
            }.ignoreElement()
            .subscribeOn(schedulers.netWorkThread())
    }

    fun getToken():UserToken?{
        return userDatabase.getToken()
    }

    fun isLogin():Maybe<UserToken>{
        return Maybe.create<UserToken> {
            val token = userDatabase.getToken()
            if(token != null){
                it.onSuccess(token)
            } else {
                it.onError(IllegalAccessException())
            }
            it.onComplete()
        }.subscribeOn(schedulers.diskThread())
    }

    fun refreshToken(refreshToken:String):Single<UserToken>{
        return userApi.refreshToken(refreshToken)
            .doOnSuccess {
                Completable.fromCallable {
                    userDatabase.saveToken(it)
                }.subscribeOn(schedulers.diskThread())
                    .subscribe()
            }
            .subscribeOn(schedulers.netWorkThread())
    }

    fun forgotPassword(email: String):Completable{
        return userApi.forgotPassword(email)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun updatePasswordByToken(newPassword:String, token:String):Completable{
        return userApi.updatePasswordByToken(newPassword,token)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun updatePassword(password: String, newPassword: String):Completable{
        return userApi.updatePassowrd(password, newPassword)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun requestEmailVerificationOtp(email:String): Completable{
        return userApi.requestEmailVerificationOtp(email)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun verifyEmailOtp(email: String, otp:String): Completable {
        return userApi.verifyEmailOtp(email, otp)
            .doOnSuccess {
                Completable.fromCallable {
                    userDatabase.saveToken(it)
                }.subscribeOn(schedulers.diskThread())
                    .subscribe()
            }.ignoreElement()
            .subscribeOn(schedulers.netWorkThread())
    }

    fun requestChangePasswordOtp(email:String): Completable{
        return userApi.requestChangePasswordOtp(email)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun updatePasswordByOtp(email:String, password:String, otp:String): Completable{
        return userApi.updatePasswordByOtp(email, password, otp)
            .subscribeOn(schedulers.netWorkThread())
    }

    fun updateUser(user: User):Completable{
        return userApi.updateUser(user)
            .subscribeOn(schedulers.netWorkThread())
    }

    suspend fun deleteUser(){
        withContext(ioDispatcher) {
            userDatabase.deleteAll()
        }
    }
}