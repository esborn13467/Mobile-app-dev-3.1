package com.example.mindnest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import model.Distraction
import model.User

@Database(
    entities = [User::class, Distraction::class],
    version = 2,  // Incremented version since we're adding a new table
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun distractionDao(): DistractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mindnest_db"
                )
                    .fallbackToDestructiveMigration() // Only for development!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}