package com.chamer.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Wallet : Screen("wallet", "Movimientos", Icons.AutoMirrored.Filled.ReceiptLong)
    object Transfer : Screen("transfer", "Transferir", Icons.Default.SwapHoriz)
    object Gaming : Screen("gaming", "Gaming", Icons.Default.SportsEsports)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Wallet,
    Screen.Transfer,
    Screen.Gaming,
    Screen.Profile
)
