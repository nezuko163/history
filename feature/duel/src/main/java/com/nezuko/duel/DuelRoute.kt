package com.nezuko.duel

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "DuelRoute"

@Composable
fun DuelRoute(
    onNavigateToQuestion: () -> Unit,
    modifier: Modifier = Modifier,
    vm: DuelViewModel = hiltViewModel()
) {
    val currentRoom by vm.currentRoom.collectAsState()
    val me by vm.me.collectAsState()
    val opponent by vm.opponent.collectAsState()
    val questions by vm.questions.collectAsState()

    if (currentRoom == null) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(text = "что?", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    LaunchedEffect(Unit) {
        if (currentRoom!!.player1 == me.data!!.id) {
            vm.findOpponent(currentRoom!!.player2)
        } else {
            vm.findOpponent(currentRoom!!.player1)
        }
    }

    Log.i(TAG, "DuelRoute: opponent - $opponent")
    Log.i(TAG, "DuelRoute: questions - $questions")

    if (opponent == null || questions == null) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(text = "загрузка", modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Log.i(TAG, "DuelRoute: $questions")
        DuelScreen(
            modifier = modifier,
            me = me.data!!,
            opponent = opponent!!,
            onButtonEndGameClick = {
                vm.endGame()
            },
            onQuestionButtonClick = onNavigateToQuestion
        )
    }
}