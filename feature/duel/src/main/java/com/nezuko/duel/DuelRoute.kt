package com.nezuko.duel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DuelRoute(
    modifier: Modifier = Modifier,
    roomId: String,
    vm: DuelViewModel = hiltViewModel()
) {
    DuelScreen(
        modifier = modifier,
        roomId = roomId,
        onButtonEndGameClick = {
            vm.endGame()
        }
    )
}