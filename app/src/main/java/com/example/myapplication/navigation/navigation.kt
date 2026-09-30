package com.example.myapplication.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screen.account.AccountScreen
import com.example.myapplication.ui.screen.auth.CreateAccountScreen
import com.example.myapplication.ui.screen.auth.CreatePasswordScreen
import com.example.myapplication.ui.screen.auth.EmailLoginScreen
import com.example.myapplication.ui.screen.auth.LoginScreen
import com.example.myapplication.ui.screen.auth.OtpScreen
import com.example.myapplication.ui.screen.home.HomeScreen
import com.example.myapplication.ui.screen.onboarding.LocationPermissionScreen
import com.example.myapplication.ui.screen.onboarding.OnboardingScreen
import com.example.myapplication.ui.screen.onboarding.SelectCountryScreen
import com.example.myapplication.ui.screen.onboarding.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val countryPreferences = context.getSharedPreferences("app_preferences", 0)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
            }
        }
        composable("onboarding") {
            OnboardingScreen {
                navController.navigate("country") { popUpTo("onboarding") { inclusive = true } }
            }
        }
        composable("country") {
            SelectCountryScreen(
                initialCountryCode = countryPreferences.getString("country_code", "VN"),
                onContinueClick = { country ->
                    countryPreferences.edit().putString("country_code", country.code).apply()
                    navController.navigate("location") { popUpTo("country") { inclusive = true } }
                }
            )
        }
        composable("location") {
            LocationPermissionScreen {
                navController.navigate("main") { popUpTo("location") { inclusive = true } }
            }
        }
        composable("main") { HomeScreen(onAccountClick = { navController.navigate("account") }) }
        composable("account") {
            AccountScreen(
                onHomeClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onCloseClick = { navController.popBackStack() },
                onCreateAccountClick = { navController.navigate("register") },
                onGoogleClick = { },
                onAppleClick = { },
                onLoginClick = { navController.navigate("loginForm") }
            )
        }
        composable("register") {
            CreateAccountScreen(
                onCloseClick = { navController.popBackStack() },
                onContinueClick = { email, sessionId ->
                    navController.navigate("otp/${Uri.encode(email)}/${Uri.encode(sessionId)}")
                },
                onLoginClick = { navController.navigate("loginForm") }
            )
        }
        composable("otp/{email}/{sessionId}") { entry ->
            OtpScreen(
                email = entry.arguments?.getString("email").orEmpty(),
                registrationSessionId = entry.arguments?.getString("sessionId").orEmpty(),
                onCloseClick = { navController.popBackStack() },
                onOtpComplete = { sessionId ->
                    navController.navigate("createPassword/${Uri.encode(sessionId)}")
                }
            )
        }
        composable("createPassword/{sessionId}") { entry ->
            CreatePasswordScreen(
                registrationSessionId = entry.arguments?.getString("sessionId").orEmpty(),
                onCloseClick = { navController.popBackStack() },
                onCompleted = {
                    navController.navigate("main") { popUpTo("account") { inclusive = false } }
                }
            )
        }
        composable("loginForm") {
            EmailLoginScreen(
                onCloseClick = { navController.popBackStack() },
                onLoggedIn = {
                    navController.navigate("main") { popUpTo("account") { inclusive = false } }
                }
            )
        }
    }
}
