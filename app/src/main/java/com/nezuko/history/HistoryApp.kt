package com.nezuko.history

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nezuko.auth.Start
import com.nezuko.auth.authScreen
import com.nezuko.auth.loginScreen
import com.nezuko.auth.navigateToLogin
import com.nezuko.auth.navigateToRegister
import com.nezuko.auth.registerScreen
import com.nezuko.home.navigation.Home
import com.nezuko.home.navigation.homeScreen
import com.nezuko.home.navigation.navigateToHome
import com.nezuko.profile.navigation.Profile
import com.nezuko.profile.navigation.navigateToProfile
import com.nezuko.profile.navigation.profileScreen
import com.nezuko.ui.theme.GrayText
import com.nezuko.ui.theme.LightBlue

private val TAG = "HistoryApp"

@Composable
fun HistoryApp(
    modifier: Modifier = Modifier,
    startDestination: Any = Start,
    navController: NavHostController = rememberNavController()
) {

    var currentRoute by remember {
        mutableStateOf(startDestination)
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteReal = currentBackStackEntry?.destination?.route
    Log.i(TAG, "HistoryApp: $currentRouteReal")
    val bottomBarVisible = when (currentRouteReal) {
        "com.nezuko.home.navigation.Home" -> true
        "com.nezuko.profile.navigation.Profile" -> true
        else -> false
    }
    Scaffold(
        bottomBar = {
            if (bottomBarVisible) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    changeRoute = { currentRoute = it })
            }
        }
    ) { padding ->
        Log.i(TAG, "HistoryApp: $padding")
        NavHost(
            modifier = modifier.padding(bottom = padding.calculateBottomPadding()),
            navController = navController,
            startDestination = startDestination,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None }
        ) {
            registerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAuthSuccess = {

                }
            )

            loginScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAuthSuccess = {}
            )

            authScreen(
                onNavigateToRegisterScreen = {
                    navController.navigateToRegister()
                },
                onNavigateToLoginScreen = {
                    navController.navigateToLogin()
                }
            )

            homeScreen()

            profileScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: Any,
    changeRoute: (Any) -> Unit
) {
    Log.i(TAG, "BottomNavigationBar: $currentRoute")

    Column {

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 30.dp),
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.3f)
        )

        NavigationBar(
            modifier = Modifier,
            containerColor = Color.Transparent,
        ) {
            NavigationBarItem(
                selected = currentRoute is Home,
                onClick = {
                    changeRoute(Home)
                    navController.navigateToHome {
                        popUpTo(0)
                    }
                },
                label = {
                    Text(text = "Главная")
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LightBlue,
                    selectedTextColor = LightBlue,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = GrayText,
                    unselectedTextColor = GrayText
                ),
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) }
            )

            NavigationBarItem(
                selected = currentRoute is Profile,
                onClick = {
                    changeRoute(Profile)
                    navController.navigateToProfile {
                        popUpTo(0)
                    }
                },
                label = {
                    Text(text = "Профиль")
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LightBlue,
                    selectedTextColor = LightBlue,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = GrayText,
                    unselectedTextColor = GrayText
                ),
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) }
            )
        }
    }
}