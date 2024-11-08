package com.nezuko.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.nezuko.ui.theme.LightBlue
import com.nezuko.ui.theme.Spacing

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPlayButtonClick: () -> Unit,
) {
    Log.i(TAG, "HomeScreen: recomp")
    Box(modifier.fillMaxSize()) {
        Button(
            onClick = onPlayButtonClick,
            modifier = Modifier
                .align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue
            ),
        ) {
            Text(
                text = "Играть",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(Spacing.default.medium)
            )
        }
    }
}