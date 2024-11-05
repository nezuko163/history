package com.nezuko.auth.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nezuko.auth.screens.StartScreen

@Composable
fun AuthRoute(
    modifier: Modifier = Modifier,
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit
) {
    StartScreen(
        onSignInClick = onNavigateToLoginScreen,
        onSignUpClick = onNavigateToRegisterScreen
    )
}