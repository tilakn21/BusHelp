package com.example.busbee.ui.screens.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementDashboardScreen(
    onTrackBusClick: () -> Unit,
    onSpeedAlertsClick: () -> Unit,
    onCameraViewClick: () -> Unit,
    onBusManagementClick: () -> Unit,
    onRoutePlanningClick: () -> Unit,
    onDriverDetailsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(title = "Live Bus Tracking", onClick = onTrackBusClick)
            DashboardCard(title = "Bus Management", onClick = onBusManagementClick)
            DashboardCard(title = "Speed & Alerts", onClick = onSpeedAlertsClick)
            DashboardCard(title = "Camera View", onClick = onCameraViewClick)

            DashboardCard(title = "Route Planning", onClick = onRoutePlanningClick)
            DashboardCard(title = "Driver/Conductor Details", onClick = onDriverDetailsClick)
        }
    }
}

@Composable
fun DashboardCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = title, fontSize = 18.sp, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagementDashboardPreview() {
    ManagementDashboardScreen(
        onTrackBusClick = {},
        onSpeedAlertsClick = {},
        onCameraViewClick = {},
        onBusManagementClick = {},
        onRoutePlanningClick = {},
        onDriverDetailsClick = {}
    )
}
