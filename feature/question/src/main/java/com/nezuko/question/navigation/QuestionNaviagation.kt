package com.nezuko.question.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nezuko.question.QuestionRoute
import kotlinx.serialization.Serializable

@Serializable
object Question

fun NavController.navigateToQuestion(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Question, navOptions)

fun NavGraphBuilder.questionScreen(
    onNavigateBack: () -> Unit
) = composable<Question> {
    QuestionRoute()
}