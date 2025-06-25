package com.example.busbee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.busbee.data.Bus
import com.example.busbee.data.BusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusManagementViewModel(private val repository: BusRepository) : ViewModel() {

    private val _busList = MutableStateFlow<List<Bus>>(emptyList())
    val busList: StateFlow<List<Bus>> get() = _busList

    init {
        fetchBuses()
    }

    fun fetchBuses() {
        viewModelScope.launch {
            repository.getBuses().collect { buses ->
                _busList.value = buses
                Log.d("BusManagementViewModel", "Fetched buses: $buses") // Debug log
            }
        }
    }


    fun addBus(bus: Bus) {
        viewModelScope.launch {
            repository.addBus(bus)
            fetchBuses()
        }
    }

    fun updateBus(busId: String, updatedBus: Bus) {
        viewModelScope.launch {
            repository.updateBus(busId, updatedBus)
            fetchBuses()
        }
    }

    fun deleteBus(busId: String) {
        viewModelScope.launch {
            repository.deleteBus(busId)
            fetchBuses()
        }
    }
}

class BusManagementViewModelFactory(private val repository: BusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusManagementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusManagementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
