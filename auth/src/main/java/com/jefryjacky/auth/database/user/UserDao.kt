package com.jefryjacky.auth.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.jefryjacky.auth.database.user.entity.UserTokenDb

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun saveUserToken(userToken: UserTokenDb)
    @Query("SELECT * FROM UserTokenDb WHERE id = 1")
    fun getUserToken():UserTokenDb?
    @Query("DELETE FROM UserTokenDb WHERE id = 1")
    fun deleteAll()
}