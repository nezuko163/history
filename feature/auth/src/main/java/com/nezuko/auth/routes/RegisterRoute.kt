package com.nezuko.auth.routes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.auth.AuthViewModel
import com.nezuko.auth.screens.RegisterScreen

@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier,
    vm: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    RegisterScreen(
        modifier = modifier
    ) { email, name, password ->
        vm.createUserWithEmailAndPassword(email, name, password)
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
    }
}