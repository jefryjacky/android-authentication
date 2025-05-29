package com.jefryjacky.auth.api.user

import io.reactivex.Single
import com.jefryjacky.auth.api.user.response.TokenResponse
import com.jefryjacky.auth.api.user.response.UserResponse
import io.reactivex.Completable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("/api/oauth/token")
    @FormUrlEncoded
    fun login(@Field("email") email:String, @Field("password") password:String):Single<TokenResponse>

    @POST("/api/oauth/google/token")
    @FormUrlEncoded
    fun loginGoogle(@Field("token") token:String):Single<TokenResponse>

    @POST("/api/oauth/token")
    @FormUrlEncoded
    fun refreshToken(@Field("grant_type") grantType:String, @Field("token") token:String):Single<TokenResponse>

    @POST("/api/user/register")
    @FormUrlEncoded
    fun register(@Field("email") email:String, @Field("password")password:String):Completable

    @GET("/api/user/get")
    fun getUser():Single<UserResponse>

    @POST("/api/user/requestemailverification")
    @FormUrlEncoded
    fun requestEmailVerification(@Field("email") email:String):Completable

    @POST("/api/user/emailverification")
    @FormUrlEncoded
    fun verifyEmail(@Field("token") token: String):Single<TokenResponse>

    @POST("/api/password/reset")
    @FormUrlEncoded
    fun forgotPassword(@Field("email") email: String):Completable

    @POST("/api/password/update/token")
    @FormUrlEncoded
    fun updatePasswordByToken(@Field("new_password") newPassword:String, @Field("token") token:String):Completable

    @POST("/api/password/update")
    @FormUrlEncoded
    fun updatePassword(@Field("password") password:String, @Field("new_password") newPassword:String):Completable

    @POST("/api/user//requestemailverification/otp")
    @FormUrlEncoded
    fun requestEmailVerificationOtp(@Field("email") email:String): Completable

    @POST("/api/user//verify/email/otp")
    fun verifyEmailOtp(@Field("email") email: String, @Field("otp") otp:String):Single<TokenResponse>

    @POST("/api/password/requestchangepassword/otp")
    fun requestChangePasswordOtp(@Field("email") email:String): Completable

    @POST("/api/password/update/otp")
    fun updatePasswordByOtp(@Field("email") email: String, @Field("otp") otp:String): Completable
}