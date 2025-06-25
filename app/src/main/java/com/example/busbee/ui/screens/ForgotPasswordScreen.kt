package com.example.busbee.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(onBackClick: () -> Unit, onResetSuccess: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var message by remember { mutableStateOf<String?>(null) }
    var isEmailSent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }

    // Regex for validating email format
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

    // Function to validate email in real-time
    fun isValidEmail(email: String): Boolean {
        return email.matches(emailRegex)
    }

    // Handle email change with validation
    fun onEmailChange(newEmail: TextFieldValue) {
        email = newEmail
        emailError = if (isValidEmail(newEmail.text)) "" else "Please enter a valid email address"
        message = null // Clear message when user edits email
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Reset Password", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Enter your email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError.isNotEmpty(),
            singleLine = true
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.text.isEmpty()) {
                    message = "Please enter your email."
                    return@Button
                }
                if (emailError.isNotEmpty()) {
                    message = emailError
                    return@Button
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.text)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message = "Password reset email sent!"
                            isEmailSent = true
                            Log.d("ForgotPassword", "Password reset email sent to ${email.text}")
                        } else {
                            message = "Failed to send reset email."
                            Log.e("ForgotPassword", "Failed to send reset email", task.exception)
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isEmailSent
        ) {
            Text("Reset Password")
        }
        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBackClick) {
            Text("Back to Login")
        }
    }
}