package com.example.busbee.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentHomeScreen(
    onChangePickupClick: () -> Unit,
    onFindAlternativeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF2575FC))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // Bus Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.DirectionsBus, contentDescription = "Bus", tint = Color.Blue)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Bus Number: 12A", fontSize = 18.sp, color = Color.Black)
                        Text(text = "Arrival: 10:15 AM", fontSize = 16.sp, color = Color.Gray)
                        Text(text = "Status: Arriving", fontSize = 16.sp, color = Color.Green)
                        Text(text = "Next Stop: Mission St & 4th St", fontSize = 16.sp, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // Driver Details Card
                Card(
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "Driver", tint = Color.Blue)
                        Text(text = "Driver: John Doe", fontSize = 16.sp, color = Color.Black)
                        Text(text = "Phone: +123 456 7890", fontSize = 14.sp, color = Color.Gray)
                        IconButton(onClick = { /* Call Driver */ }) {
                            Icon(Icons.Filled.Phone, contentDescription = "Call", tint = Color.Green)
                        }
                    }
                }

                // Pickup Options Card
                Card(
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Pickup Point", tint = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onChangePickupClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Change Pickup Point")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onFindAlternativeClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Find Alternative Buses")
                        }
                    }
                }
            }
        }

        // Bottom Navigation Bar
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                shape = RoundedCornerShape(50),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Navigate to Home */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = { /* Navigate to Track */ }) {
                        Icon(Icons.Filled.Map, contentDescription = "Track")
                    }
                    IconButton(onClick = { /* Placeholder */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "More")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStudentHomeScreen() {
    StudentHomeScreen(
        onChangePickupClick = {},
        onFindAlternativeClick = {}
    )
}
