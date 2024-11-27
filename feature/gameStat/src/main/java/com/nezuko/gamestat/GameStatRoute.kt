package com.nezuko.gamestat

import android.util.Log
import androidx.activity.compose.BackHandler
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
import kotlinx.coroutines.launch

private const val TAG = "GameStatRoute"

@Composable
fun GameStatRoute(
    roomId: String,
    modifier: Modifier = Modifier,
    vm: GameStatViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    BackHandler {
        onNavigateBack()
    }

    Log.e(TAG, "GameStatRoute: aue")

    val me by vm.me.collectAsState()
    val opponent by vm.opponent.collectAsState()
    val room by vm.room.collectAsState()
    val questions by vm.questions.collectAsState()

    LaunchedEffect(Unit) {
        vm.findMe()
    }

    LaunchedEffect(roomId) {
        vm.findRoom(roomId)
    }

    LaunchedEffect(room, me) {
        if (room == null) return@LaunchedEffect
        launch { vm.findQuestions() }
        if (me == null) return@LaunchedEffect
        launch { vm.findOpponent(if (me!!.id == room!!.player1) room!!.player2 else room!!.player1) }
    }

    if (me == null) {
        Log.e(TAG, "GameStatRoute: me = null")
        LoadingScreen()
        return
    }

    if (opponent == null) {
        Log.e(TAG, "GameStatRoute: opponent = null")
        LoadingScreen()
        return
    }

    if (room == null) {
        Log.e(TAG, "GameStatRoute: room = null")
        LoadingScreen()
        return
    }

    if (questions == null) {
        Log.e(TAG, "GameStatRoute: questions = null")
        LoadingScreen()
        return
    }

    Log.i(TAG, "GameStatRoute: questions - $questions")


    GameStatScreen(
        room = room!!,
        me = me!!,
        opponent = opponent!!,
        questions = questions!!,
        onNavigateBack = onNavigateBack,
        onMoreClick = { },
    )
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "Загрузка", modifier = Modifier.align(Alignment.Center))
    }
}