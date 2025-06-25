package com.example.busbee.ui.screens.management

import androidx.compose.foundation.background
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.busbee.data.Bus
import com.example.busbee.data.BusRepository
import com.example.busbee.ui.viewmodel.BusManagementViewModel
import com.example.busbee.ui.viewmodel.BusManagementViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusManagementScreen(
    onBackClick: () -> Unit,
    viewModel: BusManagementViewModel = viewModel(factory = BusManagementViewModelFactory(BusRepository()))
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showBusDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var selectedBus by remember { mutableStateOf<Bus?>(null) }
    val expandedState = remember { mutableStateMapOf<String, Boolean>() }
    val busList by viewModel.busList.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchBuses()
        Log.d("BusManagementScreen", "Fetching bus list")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF2575FC))))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Bus Management",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                isEditing = false
                selectedBus = null
                showBusDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Bus", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Bus", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(busList.filter { it.busNumber.contains(searchQuery.text, ignoreCase = true) }) { bus ->
                BusItem(
                    bus = bus,
                    isExpanded = expandedState[bus.id] ?: false,
                    onToggleExpand = { expandedState[bus.id] = !(expandedState[bus.id] ?: false) },
                    onEdit = {
                        Log.d("BusManagementScreen", "Editing bus: ${bus.id}")
                        isEditing = true
                        selectedBus = bus
                        showBusDialog = true
                    },
                    onDelete = {
                        Log.d("BusManagementScreen", "Deleting bus: ${bus.id}")
                        viewModel.deleteBus(bus.id)
                    }
                )
            }
        }

        if (showBusDialog) {
            AddOrEditBusDialog(
                bus = selectedBus,
                isEditing = isEditing,
                onDismiss = { showBusDialog = false },
                onSave = { newBus ->
                    if (isEditing) {
                        Log.d("BusManagementScreen", "Updating bus: ${newBus.id}")
                        viewModel.updateBus(newBus.id, newBus)
                    } else {
                        Log.d("BusManagementScreen", "Adding new bus: ${newBus.id}")
                        viewModel.addBus(newBus)
                    }
                    showBusDialog = false
                }
            )
        }
    }
}

@Composable
fun BusItem(
    bus: Bus,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggleExpand() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Bus Id: ${bus.id}", color = Color.White, fontSize = 18.sp)
            Text(text = "Status: ${bus.status}", color = Color.LightGray)

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Bus Number: ${bus.busNumber}", color = Color.White)
                Text(text = "Driver: ${bus.driverName}", color = Color.White)
                Text(text = "Phone: ${bus.driverPhone}", color = Color.LightGray)
                Text(text = "Age: ${bus.driverAge}", color = Color.LightGray)
                Text(text = "Kilometers: ${bus.kilometers}", color = Color.White)
                Text(text = "Condition: ${bus.condition}", color = Color.LightGray)

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.Green)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
@Composable
fun AddOrEditBusDialog(bus: Bus?, isEditing: Boolean, onDismiss: () -> Unit, onSave: (Bus) -> Unit) {
    var busNumber by remember { mutableStateOf(bus?.busNumber ?: "") }
    var driverName by remember { mutableStateOf(bus?.driverName ?: "") }
    var driverPhone by remember { mutableStateOf(bus?.driverPhone ?: "") }
    var driverAge by remember { mutableStateOf(bus?.driverAge?.toString() ?: "") }
    var kilometers by remember { mutableStateOf(bus?.kilometers?.toString() ?: "") }
    var condition by remember { mutableStateOf(bus?.condition ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edit Bus" else "Add New Bus", color = Color.White) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = busNumber, onValueChange = { busNumber = it }, label = { Text("Bus Number") })
                OutlinedTextField(value = driverName, onValueChange = { driverName = it }, label = { Text("Driver Name") })
                OutlinedTextField(value = driverPhone, onValueChange = { driverPhone = it }, label = { Text("Driver Phone") })

                OutlinedTextField(
                    value = driverAge,
                    onValueChange = { driverAge = it },
                    label = { Text("Driver Age") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = kilometers,
                    onValueChange = { kilometers = it },
                    label = { Text("Kilometers Driven") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(value = condition, onValueChange = { condition = it }, label = { Text("Condition Notes") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val newBus = com.example.busbee.data.Bus( // Use full package name
                    id = bus?.id ?: "",
                    busNumber = busNumber,
                    driverName = driverName,
                    driverPhone = driverPhone,
                    driverAge = driverAge.toIntOrNull() ?: 0,
                    kilometers = kilometers.toIntOrNull() ?: 0,
                    lastServiceDate = bus?.lastServiceDate ?: "",
                    condition = condition
                )
                onSave(newBus)

            }) {
                Text(if (isEditing) "Update" else "Add")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        containerColor = Color(0xFF1E1E1E)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBusManagementScreen() {
    BusManagementScreen(onBackClick = {})
}
