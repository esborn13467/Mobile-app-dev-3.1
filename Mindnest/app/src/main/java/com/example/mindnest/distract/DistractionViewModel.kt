package com.example.mindnest.distract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindnest.data.AppDatabase
import com.example.mindnest.data.DistractionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Distraction

// ViewModel used to manage and expose distraction data to the UI
class DistractionViewModel(application: Application) : AndroidViewModel(application) {

    // Repository instance to handle data access
    private val repository: DistractionRepository

    // Backing property to store the current distraction
    private val _distraction = MutableStateFlow<Distraction?>(null)

    // Public exposure of distraction state
    val distraction: StateFlow<Distraction?> get() = _distraction

    // Initializes the database and loads distraction data
    init {
        val dao = AppDatabase.getDatabase(application).distractionDao()
        repository = DistractionRepository(dao)
        seedDataAndFetch()
    }

    // Seeds the database (only once) and loads one distraction
    private fun seedDataAndFetch() {
        viewModelScope.launch {
            repository.seedDistractions()
            _distraction.value = repository.getRandomDistraction()
        }
    }

    // Refreshes the displayed distraction with a new random one
    fun refreshDistraction() {
        viewModelScope.launch {
            _distraction.value = repository.getRandomDistraction()
        }
    }
}