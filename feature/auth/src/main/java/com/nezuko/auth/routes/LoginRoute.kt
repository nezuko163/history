package com.nezuko.auth.routes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.auth.AuthViewModel
import com.nezuko.auth.screens.LoginScreen

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    vm: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    LoginScreen(
        modifier = modifier
    ) { email, password ->
        vm.signInWithEmailAndPassword(email, password)
    }
}