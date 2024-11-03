package com.nezuko.ui.theme

sealed class Screen(public val route: String) {
    data object Home : Screen("home")
    data object Auth : Screen("auth")
}