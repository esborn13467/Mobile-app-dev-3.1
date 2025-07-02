package com.example.mindnest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import model.Distraction

// DAO interface that defines how we interact with the distractions table
@Dao
interface DistractionDao {

    // Selects one random distraction from the table
    @Query("SELECT * FROM distractions ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomDistraction(): Distraction

    // Inserts a list of distractions into the database
    // Replaces existing items if there's a conflict on primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(distractions: List<Distraction>)
}
