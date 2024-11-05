package com.nezuko.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nezuko.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
object Home

fun NavController.navigateToHome(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Home, navOptions)

fun NavGraphBuilder.homeScreen(
) = composable<Home> {
    HomeRoute()
}