package model

import android.content.Context
import com.example.mindnest.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun registerUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                userDao.insertUser(user) > 0
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUser(email, password)
        }
    }
}