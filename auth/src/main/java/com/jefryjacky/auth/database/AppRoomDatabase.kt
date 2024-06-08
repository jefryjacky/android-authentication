package com.jefryjacky.auth.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jefryjacky.auth.database.user.UserDao
import com.jefryjacky.auth.database.user.entity.UserTokenDb

@Database(entities = [UserTokenDb::class], version = 1, exportSchema = true)
abstract class AppRoomDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile private var INSTANCE:AppRoomDatabase? = null

        fun getInstance(context:Context):AppRoomDatabase =
            INSTANCE?: synchronized(this){
                INSTANCE?: buildDatabase(context).also{ INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context,AppRoomDatabase::class.java, "auth.db").build()
    }
}