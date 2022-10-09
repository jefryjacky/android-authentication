package com.jefryjacky.auth.database.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jefryjacky.auth.domain.entity.UserToken

@Entity
data class UserTokenDb(
    @PrimaryKey
    var id: Int = 1,
    @ColumnInfo(name = "access_token")
    val accessToken:String,
    @ColumnInfo(name = "refresh_token")
    val refreshToken:String,
    @ColumnInfo(name = "expired_date")
    val expiredDate:Long){

    fun toUserToken():UserToken{
        return UserToken(
            accessToken, refreshToken, expiredDate
        )
    }
}