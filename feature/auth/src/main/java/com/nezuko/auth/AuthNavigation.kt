package com.nezuko.auth

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.nezuko.auth.routes.AuthRoute
import com.nezuko.auth.routes.LoginRoute
import com.nezuko.auth.routes.RegisterRoute
import kotlinx.serialization.Serializable

@Serializable
object Start

fun NavController.navigateToAuth(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Start, navOptions)

fun NavGraphBuilder.authScreen(
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
) = composable<Start>(
    enterTransition = {
        slideInHorizontally(initialOffsetX = { it })
    },
    exitTransition = {
        slideOutHorizontally(targetOffsetX = { -it })
    },
    popEnterTransition = {
        slideInHorizontally(initialOffsetX = { -it })
    },
    popExitTransition = {
        slideOutHorizontally(targetOffsetX = { it })
    }

) {
    AuthRoute(
        onNavigateToLoginScreen = onNavigateToLoginScreen,
        onNavigateToRegisterScreen = onNavigateToRegisterScreen
    )
}

@Serializable
object Login

fun NavController.navigateToLogin(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Login, navOptions)

fun NavGraphBuilder.loginScreen(
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) = composable<Login>(
    enterTransition = {
        slideInHorizontally(initialOffsetX = { it })
    },
    exitTransition = {
        slideOutHorizontally(targetOffsetX = { -it })
    },
    popEnterTransition = {
        slideInHorizontally(initialOffsetX = { -it })
    },
    popExitTransition = {
        slideOutHorizontally(targetOffsetX = { it })
    }
) {
    LoginRoute(
        onNavigateBack = onNavigateBack,
        onAuthSuccess = onAuthSuccess
    )
}

@Serializable
object Register

fun NavController.navigateToRegister(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(Register, navOptions)

fun NavGraphBuilder.registerScreen(
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) = composable<Register>(
    enterTransition = {
        slideInHorizontally(initialOffsetX = { it })
    },
    exitTransition = {
        slideOutHorizontally(targetOffsetX = { -it })
    },
    popEnterTransition = {
        slideInHorizontally(initialOffsetX = { -it })
    },
    popExitTransition = {
        slideOutHorizontally(targetOffsetX = { it })
    }
) {
    RegisterRoute(
        onNavigateBack = onNavigateBack,
        onAuthSuccess = onAuthSuccess
    )
}