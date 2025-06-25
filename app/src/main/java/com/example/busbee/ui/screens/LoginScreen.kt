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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busbee.GoogleSignInHelper.GoogleSignInHelper
import com.example.busbee.dialog.SuccessDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: (String) -> Unit
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
    var isLoading by remember { mutableStateOf(false) }


    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GoogleSignIn", "Activity result code: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
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
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    fun isValidEmail(email: String): Boolean = email.matches(emailRegex)

    fun onEmailChange(newEmail: TextFieldValue) {
        email = newEmail
        emailError = if (isValidEmail(newEmail.text)) "" else "Please enter a valid email address"
        loginError = ""
    }

    fun onLogin(email: String, password: String) {
        if (!isValidEmail(email)) {
            loginError = "Invalid email format."
            return
        }

        isLoading = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    Log.d("Login", "Success: Logged in!")
                    loginError = ""
                    loginSuccessDialogVisible = true
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                        is FirebaseAuthInvalidUserException -> "No account found with this email."
                        else -> "Login failed. Please try again."
                    }
                    Log.d("Login", "Failure: $errorMessage")
                    loginError = errorMessage
                }
            }
    }

    fun onForgotPassword(email: String) {
        if (!isValidEmail(email)) {
            emailError = "Please enter a valid email."
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ForgotPassword", "Password reset email sent successfully.")
                    loginError = "Password reset email sent!"
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.e("ForgotPassword", "Error sending reset email: $errorMessage")
                    loginError = "Failed to send reset email: $errorMessage"
                }
            }

    }

    // Delayed navigation after success
    LaunchedEffect(loginSuccessDialogVisible) {
        if (loginSuccessDialogVisible) {
            delay(1500) // Show dialog for 1.5 seconds
            loginSuccessDialogVisible = false
            onLoginClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF2575FC))
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bus Help", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            Text(text = emailError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp))
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

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = { onForgotPassword(email.text) }) {
            Text("Forgot Password?", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Button(
                onClick = { onLogin(email.text, password.text) },
                modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }
        }

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp, top = 8.dp))
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
