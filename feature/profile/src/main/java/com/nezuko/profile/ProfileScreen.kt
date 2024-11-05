package com.nezuko.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

private const val TAG = "ProfileScreen"

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Log.i(TAG, "ProfileScreen: recomp")
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "Profile", modifier = Modifier.align(Alignment.Center))
    }
}