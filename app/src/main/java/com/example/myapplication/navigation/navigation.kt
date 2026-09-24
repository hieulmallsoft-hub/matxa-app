package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screen.OnboardingScreen
import com.example.myapplication.ui.screen.SplashScreen
import com.example.myapplication.ui.theme.screen.RegionScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onOnboardingFinished = {
                    navController.navigate("onRegion") {
                        popUpTo("onboarding") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("onRegion") {
            RegionScreen()
        }
    }
}
