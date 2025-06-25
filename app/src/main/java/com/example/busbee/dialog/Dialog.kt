package com.example.busbee.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SuccessDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                color = Color.Black // Adjust color as needed
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                color = Color.DarkGray // Adjust color as needed
            )
        },confirmButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp), // Rounded corners for button
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF121212) // Match button text color with your theme
                )
            ) {
                Text("OK")
            }
        },

        modifier = Modifier
            .clip(RoundedCornerShape(16.dp)) // Rounded corners for dialog
            .background(Color.White), // Background color of the dialog
        properties = DialogProperties(usePlatformDefaultWidth = false) // Full width if needed
    )
}

@Preview(showBackground = true)
@Composable
fun SuccessDialogPreview() {
    SuccessDialog(
        title = "Success",
        message = "You have registered successfully!",
        onDismiss = {}
    )
}
