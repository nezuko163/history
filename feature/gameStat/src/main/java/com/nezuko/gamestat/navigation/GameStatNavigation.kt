package com.nezuko.gamestat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nezuko.gamestat.GameStatRoute
import kotlinx.serialization.Serializable

@Serializable
data class GameStat(val roomId: String)

fun NavController.navigateToGameStat(
    roomId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(GameStat(roomId), navOptions)

fun NavGraphBuilder.gameStatScreen(
    onNavigateBack: () -> Unit
) = composable<GameStat> { navBackStackEntry ->
    val route: GameStat = navBackStackEntry.toRoute()

    GameStatRoute(
        roomId = route.roomId,
        onNavigateBack = onNavigateBack
    )
}