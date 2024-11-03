package com.nezuko.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun App(
    modifier: Modifier = Modifier,
    startDestination: Any,
    navController: NavController = rememberNavController()
) {

    var currentRoute by remember {
        mutableStateOf(startDestination)
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteReal = currentBackStackEntry?.destination?.route
}

