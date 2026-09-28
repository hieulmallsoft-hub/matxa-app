package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screen.AccountScreen
import com.example.myapplication.ui.screen.LocationPermissionScreen
import com.example.myapplication.ui.screen.HomeScreen
import com.example.myapplication.ui.screen.OnboardingScreen
import com.example.myapplication.ui.screen.SelectCountryScreen
import com.example.myapplication.ui.screen.SplashScreen
import com.example.myapplication.ui.screen.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val countryPreferences = context.getSharedPreferences("app_preferences", 0)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("onboarding") {
            OnboardingScreen(
                onOnboardingFinished = {
                    navController.navigate("country") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("country") {
            SelectCountryScreen(
                initialCountryCode = countryPreferences.getString(
                    "country_code",
                    "VN"
                ),
                onContinueClick = { country ->

                    countryPreferences.edit()
                        .putString("country_code", country.code)
                        .apply()

                    navController.navigate("location") {
                        popUpTo("country") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("location") {
            LocationPermissionScreen(
                onPermissionGranted = {
                    navController.navigate("main") {
                        popUpTo("location") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("main") {
            HomeScreen(
                onAccountClick = { navController.navigate("account") }
            )


        }

        composable("account") {
            AccountScreen(
                onHomeClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                onCloseClick = {
                    navController.popBackStack()
                },

                onCreateAccountClick = {
                    navController.navigate("register")
                },

                onGoogleClick = {
                    println("Google login")
                },

                onAppleClick = {
                    println("Apple login")
                },

                onLoginClick = {
                    navController.navigate("loginForm")
                }
            )
        }

    }
}
