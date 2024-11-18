package com.nezuko.gamestat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

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

    LaunchedEffect(Unit) {
        vm.findMe()
    }

    LaunchedEffect(roomId) {
        vm.findRoom(roomId)
    }

    LaunchedEffect(room, me) {
        if (me == null || room == null) return@LaunchedEffect
        vm.findOpponent(if (me!!.id == room!!.player1) room!!.player2 else room!!.player1)
    }

    if (me == null) {
        Log.e(TAG, "GameStatRoute: me = null")
        return
    }

    if (opponent == null) {
        Log.e(TAG, "GameStatRoute: opponent = null")
        return
    }

    if (room == null) {
        Log.e(TAG, "GameStatRoute: room = null")
        return
    }


    GameStatScreen(
        room = room!!,
        me = me!!,
        opponent = opponent!!,
        onNavigateBack = onNavigateBack,
        onMoreClick = { },
    )
}