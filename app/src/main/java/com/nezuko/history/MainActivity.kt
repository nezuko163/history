package com.nezuko.history

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nezuko.auth.Start
import com.nezuko.home.navigation.Home
import com.nezuko.ui.theme.HistoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityTAG"
    private val vm: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vm.onCreate()
        setContent {
            HistoryTheme {
                val userId by vm.userId.collectAsState()
                var isLoadingEnd by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    vm.getCurrentUser()
                    isLoadingEnd = true
                    Log.i(TAG, "onCreate: loading end")
                }

                Log.i(TAG, "onCreate: $userId")

                val splashScreen = installSplashScreen()
                splashScreen.setKeepOnScreenCondition { !isLoadingEnd }


                if (isLoadingEnd) {
                    val startDestination = if (userId.data == null) Start else Home
                    HistoryApp(
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}