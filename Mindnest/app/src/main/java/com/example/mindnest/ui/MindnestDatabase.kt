package com.example.mindnest.ui

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mindnest.data.UserDao
import model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MindNestDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: MindNestDatabase? = null

        fun getDatabase(context: Context): MindNestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MindNestDatabase::class.java,
                    "mindnest_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}