package com.example.busbee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.busbee.ui.screens.*
import com.example.busbee.ui.screens.management.BusManagementScreen
import com.example.busbee.ui.screens.management.BusTrackScreen
import com.example.busbee.ui.screens.management.ManagementDashboardScreen
import com.google.firebase.auth.FirebaseAuth

private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Check if user is already logged in
            val startDestination = if (auth.currentUser != null) "management_dashboard" else "welcome"

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
                            navController.navigate("management_dashboard") {
                                popUpTo("login") { inclusive = true } // Clear backstack
                            }
                        },
                        onSignUpClick = { navController.navigate("signup") },
                        onForgotPasswordClick = { navController.navigate("forgot_password") }
                    )
                }
                composable("signup") {
                    SignUpScreen(
                        onBackClick = { navController.popBackStack() },
                        onSignUpClick = { _, _, _ ->
                            navController.navigate("management_dashboard") {
                                popUpTo("signup") { inclusive = true }
                            }
                        },
                        onLoginClick = { navController.navigate("login") }
                    )
                }
                composable("home") {
                    StudentHomeScreen(
                        onChangePickupClick = { navController.navigate("pickup_screen") },
                        onFindAlternativeClick = { navController.navigate("alternative_bus_screen") },
                        onLogoutClick = {
                            auth.signOut()
                            navController.navigate("welcome") {
                                popUpTo("home") { inclusive = true } // Clear backstack
                            }
                        },
                        onBoardBusClick = {
                            // Handle onboard bus logic
                        }
                    )
                }
                composable("forgot_password") {
                    ForgotPasswordScreen(
                        onBackClick = { navController.popBackStack() },
                        onResetSuccess = { navController.popBackStack("login", false) }
                    )
                }
                composable("management_dashboard") {
                    ManagementDashboardScreen(
                        onTrackBusClick = { navController.navigate("track_bus") },
                        onBusManagementClick = { navController.navigate("bus_management") },
                        onDriverDetailsClick = { navController.navigate("driver_conductor") },
                        onRoutePlanningClick = { navController.navigate("route_planning") },
                        onCameraViewClick = { navController.navigate("camera_view") },
                        onSpeedAlertsClick = { navController.navigate("speed_alert") }
                    )
                }
                composable("track_bus") {
                    BusTrackScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable("bus_management") {
                    BusManagementScreen(
                        onBackClick = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
