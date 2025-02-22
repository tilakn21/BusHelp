package com.example.busbee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.busbee.ui.screens.StudentHomeScreen
import com.example.busbee.ui.screens.WelcomeScreen
import com.example.busbee.ui.screens.LoginScreen
import com.example.busbee.ui.screens.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val auth = FirebaseAuth.getInstance()

            // Check if user is already logged in
            val startDestination = if (auth.currentUser != null) "home" else "welcome"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("welcome") {
                    WelcomeScreen(
                        onLoginClick = { navController.navigate("login") },
                        onSignUpClick = { navController.navigate("signup") }
                    )
                }
                composable("login") {
                    LoginScreen(
                        onBackClick = { navController.popBackStack() },
                        onLoginClick = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true } // Clear backstack
                            }
                        },
                        onSignUpClick = { navController.navigate("signup") },
                        onForgotPasswordClick = {
                            // Handle forgot password logic
                        }
                    )
                }
                composable("signup") {
                    SignUpScreen(
                        onBackClick = { navController.popBackStack() },
                        onSignUpClick = { _, _, _ ->
                            navController.navigate("home") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onLoginClick = { navController.navigate("login") }
                    )
                }
                composable("home") {
                    StudentHomeScreen(
                        onChangePickupClick = { navController.popBackStack() },
                        onFindAlternativeClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
