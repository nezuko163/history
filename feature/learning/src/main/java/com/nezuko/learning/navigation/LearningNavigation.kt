package com.nezuko.learning.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nezuko.learning.LearningRoute
import kotlinx.serialization.Serializable

@Serializable
object Learning

fun NavController.navigateToLearning(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Learning, navOptions)

fun NavGraphBuilder.learningScreen(
    onNavigateToCreateQuestion: () -> Unit
) = composable<Learning> {
    LearningRoute(onNavigateToCreateQuestion = onNavigateToCreateQuestion)
}