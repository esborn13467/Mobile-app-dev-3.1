package com.example.mindnest.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Distraction

// Repository layer that manages distraction data operations
class DistractionRepository(private val dao: DistractionDao) {

    // Seeds the database with sample distraction content
    suspend fun seedDistractions() = withContext(Dispatchers.IO) {
        val sampleDistractions = listOf(
            Distraction(type = "joke", content = "Why did the scarecrow win an award? Because he was outstanding in his field!"),
            Distraction(type = "quote", content = "Keep going, everything you need will come to you at the perfect time."),
            Distraction(type = "fact", content = "Did you know? Laughter boosts the immune system."),
            Distraction(type = "joke", content = "Parallel lines have so much in common… it’s a shame they’ll never meet.")
        )
        dao.insertAll(sampleDistractions)
    }

    // Retrieves one random distraction from the database
    suspend fun getRandomDistraction(): Distraction {
        return withContext(Dispatchers.IO) {
            dao.getRandomDistraction()
        }
    }
}