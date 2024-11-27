package com.nezuko.gamehistory

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
import com.nezuko.domain.model.RoomModel

private const val TAG = "GameHistoryRoute"

@Composable
fun GameHistoryRoute(
    id: String,
    onArrowClick: () -> Unit,
    onNavigateToGameStat: (room: RoomModel) -> Unit,
    vm: GameHistoryViewModel = hiltViewModel()
) {
    val games by vm.games.collectAsState()
    val users by vm.users.collectAsState()

    LaunchedEffect(Unit) {
        vm.getUserGames(id)
    }

    LaunchedEffect(games) {
        if (games != null) {
            vm.searchUsers((games!!.map { it.player1 } + games!!.map { it.player2 }).distinct())
        }
    }

    Log.i(TAG, "GameHistoryRoute: games - $games")
    Log.i(TAG, "GameHistoryRoute: users - $users")

    if (games == null || users == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "История игр", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    if (games!!.isEmpty() || users!!.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Игр нет", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    GameHistoryScreen(
        user = users!![id]!!,
        games = games!!,
        users = users!!,
        onGameClick = onNavigateToGameStat,
        onArrowClick = onArrowClick
    )
}