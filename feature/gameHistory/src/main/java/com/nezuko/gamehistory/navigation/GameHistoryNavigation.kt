package com.nezuko.gamehistory.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nezuko.domain.model.RoomModel
import com.nezuko.gamehistory.GameHistoryRoute
import kotlinx.serialization.Serializable

@Serializable
data class GameHistory(val userId: String)

fun NavController.navigateToGameHistory(
    userId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(GameHistory(userId), navOptions)

fun NavGraphBuilder.gameHistoryScreen(
    onNavigateToGameStat: (room: RoomModel) -> Unit,
    onNavigateBack: () -> Unit,
) = composable<GameHistory> { backStackEntry ->
    val route: GameHistory = backStackEntry.toRoute()

    GameHistoryRoute(
        id = route.userId,
        onArrowClick = onNavigateBack,
        onNavigateToGameStat = onNavigateToGameStat,
    )
}