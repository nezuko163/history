package com.nezuko.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.domain.model.RoomModel

private const val TAG = "HomeRoute"

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDuel: (room: RoomModel) -> Unit,
) {
    val me by vm.me.collectAsState()
    val isSearching by vm.isSearching.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
    }

    val startSearch = {
        vm.startSearch(
            userProfile = me!!,
            onRoomCreated = { room ->
                Toast.makeText(context, room.toString(), Toast.LENGTH_SHORT).show()
                onNavigateToDuel(room)
            },
            onGameEnd = { room ->
                Toast.makeText(context, "игра ${room.id} закончена", Toast.LENGTH_SHORT).show()
            }
        )
    }
    val stopSearch = {
        vm.stopSearch(
            userProfile = me!!,
            onSearchStopped = {
                Toast.makeText(context, "поиск закончен", Toast.LENGTH_SHORT).show()
            }
        )
    }

    val onPlayButtonClick: () -> Unit = if (isSearching) {
        stopSearch
    } else {
        startSearch
    }

    HomeScreen(
        modifier = modifier,
        isSearching = isSearching,
        onPlayButtonClick = onPlayButtonClick
    )
}