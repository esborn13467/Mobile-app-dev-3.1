package com.example.mindnest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import model.Distraction

// Room database class that holds the distraction table and provides the DAO
@Database(entities = [Distraction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Exposes the DistractionDao for queries and insertions
    abstract fun distractionDao(): DistractionDao

    companion object {
        // Ensures only one instance of the database is created (Singleton pattern)
        @Volatile private var INSTANCE: AppDatabase? = null

        // Returns the database instance, creating it if necessary
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mindnest_db" // Name of the SQLite DB
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
