package com.example.busbee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.busbee.ui.screens.WelcomeScreen
import com.example.busbee.ui.screens.LoginScreen
import com.example.busbee.ui.screens.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "welcome") {
                composable("welcome") {
                    WelcomeScreen(
                        onLoginClick = {
                            navController.navigate("login")
                        },
                            onSignUpClick = { navController.navigate("signup")
                        }
                    )
                }
                composable("login") {
                    LoginScreen(
                        onBackClick = { navController.popBackStack() },
                        onLoginClick = {
                            // Handle login logic
                        },
                        onSignUpClick = {
                            navController.navigate("signup") // If you have a signup screen
                        },
                        onForgotPasswordClick = {
                            // Navigate to forgot password screen or show dialog
                        }
                    )
                }

                composable("signup") {
                    SignUpScreen(
                        onBackClick = { navController.popBackStack() },
                        onSignUpClick = { name, email, password ->
                            // Handle sign-up logic here
                        },
                        onLoginClick = { navController.navigate("login") }
                    )
                }

            }
        }
    }
}
