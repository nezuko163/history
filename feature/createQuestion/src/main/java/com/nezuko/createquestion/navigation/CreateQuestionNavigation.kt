package com.nezuko.createquestion.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nezuko.createquestion.CreateQuestionRoute
import kotlinx.serialization.Serializable

@Serializable
object CreateQuestion

fun NavController.navigateToCreateQuestion(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(CreateQuestion, navOptions)

fun NavGraphBuilder.createQuestionScreen(
    onNavigateBack: () -> Unit
) = composable<CreateQuestion> { backStackEntry ->
    CreateQuestionRoute(
        onNavigateBack = onNavigateBack
    )
}