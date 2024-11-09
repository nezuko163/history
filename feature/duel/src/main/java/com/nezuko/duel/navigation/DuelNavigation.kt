package com.nezuko.duel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nezuko.duel.DuelRoute
import kotlinx.serialization.Serializable

@Serializable
data class Duel(val roomId: String)

fun NavController.navigateToDuel(
    roomId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Duel(roomId), navOptions)

fun NavGraphBuilder.duelScreen(
) = composable<Duel> { backStackEntry ->
    val route: Duel = backStackEntry.toRoute()
    DuelRoute(
        roomId = route.roomId
    )
}