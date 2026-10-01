package com.chamer.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chamer.app.ui.navigation.NavGraph
import com.chamer.app.ui.navigation.Screen
import com.chamer.app.ui.navigation.bottomNavScreens
import com.chamer.app.ui.theme.ChamerTheme
import com.chamer.app.ui.theme.PrimaryGreen
import com.chamer.app.viewmodel.ChamerViewModel

@Composable
fun ChamerApp(viewModel: ChamerViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensajes emergentes en el SnackbarHost
    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearUserMessage()
        }
    }

    ChamerTheme(appThemeMode = themeMode) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomBar = currentRoute != Screen.Splash.route

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        bottomNavScreens.forEach { screen ->
                            val selected = currentRoute == screen.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    screen.icon?.let { icon ->
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = screen.title
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = screen.title,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PrimaryGreen,
                                    selectedTextColor = PrimaryGreen
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavGraph(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
