package com.example.busbee.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.busbee.dialog.SuccessDialog
import com.google.firebase.auth.FirebaseAuth
import com.example.busbee.GoogleSignInHelper.GoogleSignInHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val googleSignInHelper = GoogleSignInHelper(LocalContext.current)
    val activity = LocalContext.current as Activity

    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var loginSuccessDialogVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Loading state

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            googleSignInHelper.handleSignInResult(result.data,
                onSuccess = {
                    loginSuccessDialogVisible = true
                    Log.d("GoogleSignIn", it)
                    onLoginClick()
                },
                onFailure = {
                    loginError = it
                    Log.d("GoogleSignIn", it)
                }
            )
        } else {
            loginError = "Google Sign-In was cancelled."
        }
    }


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
        loginError = "" // Clear login errors when user edits email
    }

    // Login button click action
    fun onLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            loginError = "Email and Password cannot be empty."
            return
        }

        isLoading = true // Start loading

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false // Stop loading

                if (task.isSuccessful) {
                    Log.d("Login", "Success: Logged in!")
                    loginSuccessDialogVisible = true
                    loginError = "" // Clear any previous error
                    onLoginClick() // Navigate or perform action after successful login
                } else {
                    Log.d("Login", "Failure: ${task.exception?.message}")
                    loginError = task.exception?.message ?: "Login failed. Please try again."
                }
            }
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
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(0.8f),
            isError = emailError.isNotEmpty(),
            singleLine = true
        )

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
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(0.8f),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color.White) // Show loading indicator
        } else {
            Button(
                onClick = { onLogin(email.text, password.text) },
                modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(text = "Login", fontSize = 18.sp)
            }
        }

        if (loginError.isNotEmpty()) {
            Text(
                text = loginError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
        }

        if (loginSuccessDialogVisible) {
            SuccessDialog(
                title = "Success",
                message = "You have logged in successfully!",
                onDismiss = { loginSuccessDialogVisible = false }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { googleSignInLauncher.launch(googleSignInHelper.getGoogleSignInIntent()) }) {
            Text(text = "Sign in with Google")
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
