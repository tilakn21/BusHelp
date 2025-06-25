package com.example.busbee.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.Status
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class Bus(
    val id: String = "",
    val busNumber: String = "",
    val driverName: String = "",
    val driverPhone: String = "",
    val driverAge: Int = 0,
    val kilometers: Int = 0,
    val condition: String = "",
    val status: String ="",
    val lastServiceDate: String = ""
)

class BusRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val busCollection = firestore.collection("buses")

    fun getBuses(): Flow<List<Bus>> = callbackFlow {
        val busesRef = firestore.collection("buses")
        val listener = busesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("BusRepository", "Error fetching buses", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val busList = snapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(Bus::class.java)
                    } catch (e: Exception) {
                        Log.e("BusRepository", "Error deserializing bus: ${e.message}", e)
                        null
                    }
                }
                trySend(busList).isSuccess
                Log.d("BusRepository", "Fetched buses: $busList") // Debug log
            }

        }
        awaitClose { listener.remove() }
    }


    suspend fun addBus(bus: Bus) {
        busCollection.add(bus).await()
    }

    suspend fun updateBus(busId: String, updatedBus: Bus) {
        busCollection.document(busId).set(updatedBus).await()
    }

    suspend fun deleteBus(busId: String) {
        busCollection.document(busId).delete().await()
    }
}