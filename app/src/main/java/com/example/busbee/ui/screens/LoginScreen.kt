package com.example.busbee.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }

    // Regex for validating email format
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

    // Function to validate email in real-time
    fun isValidEmail(email: String): Boolean {
        return email.matches(emailRegex)
    }

    // Login button click action
    fun onLogin() {
        // Check if email and password are valid
        if (isValidEmail(email.text)) {
            Log.d("LoginScreen", "Email: ${email.text}")
            Log.d("LoginScreen", "Password: ${password.text}")
            onLoginClick() // Trigger the provided login click action
        } else {
            Log.d("LoginScreen", "Invalid email format")
        }
    }

    // Handle email change with validation
    fun onEmailChange(newEmail: TextFieldValue) {
        email = newEmail
        emailError = if (isValidEmail(newEmail.text)) "" else "Please enter a valid email address"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Dark background
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bus Help",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.8f),
            isError = emailError.isNotEmpty(),
            singleLine = true
        )

        // Display error message if email is invalid
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                // Show/Hide password button inside the TextField
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        color = Color.Gray
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onForgotPasswordClick) {
            Text("Forgot Password?", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onLogin() }, // Trigger the login function with validation and log
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = isValidEmail(email.text) // Disable login button if email is invalid
        ) {
            Text(text = "Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onSignUpClick) {
            Text("Don't have an account? Sign Up", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onBackClick = {},
        onLoginClick = {},
        onSignUpClick = {},
        onForgotPasswordClick = {}
    )
}
