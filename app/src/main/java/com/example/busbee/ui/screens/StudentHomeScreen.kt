package com.example.busbee.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentHomeScreen(
    userName: String = "John Doe",
    busNumber: String = "12A",
    arrivalTime: String = "10:15 AM",
    busStatus: String = "Arriving",
    nextStop: String = "Mission St & 4th St",
    driverName: String = "John Doe",
    driverPhone: String = "+123 456 7890",
    onChangePickupClick: () -> Unit,
    onFindAlternativeClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBoardBusClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF2575FC))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /** Top Bar **/
            TopBar(userName = userName, onLogoutClick = onLogoutClick)

            /** Main Content **/
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BusStatusCard(busNumber, arrivalTime, busStatus, nextStop, onBoardBusClick)
                DriverDetailsCard(driverName, driverPhone)
                PickupOptionsCard(onChangePickupClick, onFindAlternativeClick)
            }

            /** Bottom Navigation **/
            BottomNavigationBar()
        }
    }
}

/** Top Bar */
@Composable
fun TopBar(userName: String, onLogoutClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Person, contentDescription = "User", tint = Color.White, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        IconButton(onClick = onLogoutClick) {
            Icon(Icons.Filled.Logout, contentDescription = "Logout", tint = Color.White, modifier = Modifier.size(36.dp))
        }
    }
}

/** Bus Status Card */
@Composable
fun BusStatusCard(busNumber: String, arrivalTime: String, busStatus: String, nextStop: String, onBoardBusClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.DirectionsBus, contentDescription = "Bus", tint = Color.Blue, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Bus Number: $busNumber", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Arrival: $arrivalTime", fontSize = 16.sp, color = Color.Gray)
                    Text(text = "Status: $busStatus", fontSize = 16.sp, color = Color.Green)
                    Text(text = "Next Stop: $nextStop", fontSize = 16.sp)
                }
            }
            Button(onClick = onBoardBusClick, modifier = Modifier.fillMaxWidth()) {
                Text("Will you board the bus?", color = Color.White)
            }
        }
    }
}

/** Driver Details Card */
@Composable
fun DriverDetailsCard(driverName: String, driverPhone: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = "Driver", tint = Color.Blue, modifier = Modifier.size(60.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Driver: $driverName", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Phone: $driverPhone", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

/** Pickup Options Card */
@Composable
fun PickupOptionsCard(onChangePickupClick: () -> Unit, onFindAlternativeClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "Pickup Point", tint = Color.Red, modifier = Modifier.size(60.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onChangePickupClick, modifier = Modifier.fillMaxWidth()) {
                Text("Change Pickup Point", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onFindAlternativeClick, modifier = Modifier.fillMaxWidth()) {
                Text("Find Alternative Buses", color = Color.White)
            }
        }
    }
}

/** Bottom Navigation */
@Composable
fun BottomNavigationBar() {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {}) { Icon(Icons.Filled.Home, contentDescription = "Home") }
            IconButton(onClick = {}) { Icon(Icons.Filled.Map, contentDescription = "Track") }
            IconButton(onClick = {}) { Icon(Icons.Filled.Menu, contentDescription = "More") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStudentHomeScreen() {
    StudentHomeScreen(
        onChangePickupClick = {},
        onFindAlternativeClick = {},
        onLogoutClick = {},
        onBoardBusClick = {}
    )
}
