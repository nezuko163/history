package com.nezuko.duel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DuelScreen(
    modifier: Modifier = Modifier,
    roomId: String,
    onButtonEndGameClick: () -> Unit
) {
    BackHandler {

    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = roomId)
        Button(onClick = onButtonEndGameClick) {
            Text(text = "закончить игру")
        }
    }
}