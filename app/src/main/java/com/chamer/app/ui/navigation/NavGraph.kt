package com.chamer.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chamer.app.ui.screens.GamingScreen
import com.chamer.app.ui.screens.HomeScreen
import com.chamer.app.ui.screens.ProfileScreen
import com.chamer.app.ui.screens.SplashScreen
import com.chamer.app.ui.screens.TransferScreen
import com.chamer.app.ui.screens.WalletScreen
import com.chamer.app.viewmodel.ChamerViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ChamerViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToWallet = {
                    navController.navigate(Screen.Wallet.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToTransfer = {
                    navController.navigate(Screen.Transfer.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToGaming = {
                    navController.navigate(Screen.Gaming.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Wallet.route) {
            WalletScreen(viewModel = viewModel)
        }

        composable(Screen.Transfer.route) {
            TransferScreen(viewModel = viewModel)
        }

        composable(Screen.Gaming.route) {
            GamingScreen(viewModel = viewModel)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(viewModel = viewModel)
        }
    }
}
