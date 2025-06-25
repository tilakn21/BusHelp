package com.example.busbee.ui.screens.management

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusTrackScreen(onBackClick: () -> Unit) {
    val busList = listOf("Bus 101", "Bus 202", "Bus 303", "Bus 404") // Sample data

    var selectedBus by remember { mutableStateOf(busList.first()) }
    var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility

    val busLocations = mapOf(
        "Bus 101" to LatLng(22.7000, 75.8500),
        "Bus 202" to LatLng(22.7200, 75.8200),
        "Bus 303" to LatLng(22.7100, 75.8100),
        "Bus 404" to LatLng(22.820660181568204, 75.942608430208)
    )


    val defaultLocation = LatLng(22.6911, 75.8283) // ✅ Default location
    val selectedBusLocation = busLocations[selectedBus] ?: defaultLocation

    val busRoutes = mapOf(
        "Bus 101" to listOf(
            LatLng(22.691135, 75.828305), // Start point
            LatLng(22.693000, 75.830000),
            LatLng(22.695500, 75.832500),
            LatLng(22.700000, 75.835000), // End point
        ),
        "Bus 202" to listOf(
            LatLng(22.701135, 75.838305),
            LatLng(22.703000, 75.840000),
            LatLng(22.705500, 75.842500),
            LatLng(22.710000, 75.845000),
        ),
        "Bus 303" to listOf(
            LatLng(22.711135, 75.848305),
            LatLng(22.713000, 75.850000),
            LatLng(22.715500, 75.852500),
            LatLng(22.720000, 75.855000),
        ),
        "Bus 404" to listOf(
            LatLng(22.68999515221974, 75.8436143508935),
            LatLng(22.68382520601688, 75.84155022348045),
            LatLng(22.750457883335894, 75.93192198099419),
            LatLng(22.820660181568204, 75.942608430208),
            LatLng(22.69015843872767, 75.843759841788),
        )
    )

    val selectedRoute = busRoutes[selectedBus] ?: emptyList()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }
    var currentBusLocation by remember { mutableStateOf(selectedBusLocation) }


    LaunchedEffect(selectedBus) {
        while (true) {
            delay(3000) // Simulating update every 3 seconds

            // Simulated movement: Slightly modify the latitude & longitude
            currentBusLocation = LatLng(
                currentBusLocation.latitude + 0.0001, // Move slightly
                currentBusLocation.longitude + 0.0001
            )
            Log.d("BusTracking", "Bus $selectedBus moved to: ${currentBusLocation.latitude}, ${currentBusLocation.longitude}")


            // Animate camera to new location
            cameraPositionState.animate(CameraUpdateFactory.newLatLng(currentBusLocation))
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF2575FC))))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Track Bus",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Fixed Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedBus,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Bus") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .menuAnchor()
                    .clickable { expanded = true }, // Ensure clicking opens dropdown
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                busList.forEach { bus ->
                    DropdownMenuItem(
                        text = { Text(bus) },
                        onClick = {
                            selectedBus = bus
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Fixed Google Map (Default Coordinates & Updates)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.2f))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                if (selectedBusLocation != defaultLocation) {
                    Marker(
                        state = MarkerState(position = selectedBusLocation),
                        title = selectedBus
                    )
                }
                if (selectedRoute.isNotEmpty()) {
                    Marker(
                        state = MarkerState(position = currentBusLocation),
                        title = "Live Bus Location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                    Polyline(
                        points = listOf(selectedBusLocation, currentBusLocation), // Past route
                        color = Color.Blue,
                        width = 8f
                    )

                    // Markers for bus stops
                    selectedRoute.forEach { stop ->
                        Marker(
                            state = MarkerState(position = stop),
                            title = "Stop"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Real-time Bus Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Bus Number: $selectedBus", color = Color.White, fontSize = 18.sp)
                Text("Student Count: 45", color = Color.LightGray)
                Text("Next Stop: Central Park", color = Color.LightGray)
                Text("Route: Downtown Express", color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bus Staff Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Driver: John Doe", color = Color.White, fontSize = 18.sp)
                Text("Conductor: Alex Smith", color = Color.LightGray)
                Text("Bus Number: $selectedBus", color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBusTrackScreen() {
    BusTrackScreen(onBackClick = {})
}
