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
                val me by vm.me.collectAsState()
                Log.i(TAG, "onCreate: me - $me")
                LaunchedEffect(Unit) {
                    vm.getCurrentUser()
                    Log.i(TAG, "onCreate: loading end")
                }

                Log.i(TAG, "onCreate: $me")
                val splashScreen = installSplashScreen()

                splashScreen.setKeepOnScreenCondition {
                    me == null
                }
                if (me != null) {
                    val startDestination = if (me!!.id.isEmpty()) {
                        Start
                    } else {
                        Home
                    }
                    HistoryApp(
                        startDestination = startDestination
                    )
                }
            }
        }
    }
    override fun onDestroy() {
        vm.onDestroy()
        super.onDestroy()
    }
}