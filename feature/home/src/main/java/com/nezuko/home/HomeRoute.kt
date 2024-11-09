package com.nezuko.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.domain.model.RoomModel

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDuel: (room: RoomModel) -> Unit,
    onNavigateBack: () -> Unit
) {
    val me by vm.me.collectAsState()
    val context = LocalContext.current

    HomeScreen(
        modifier = modifier,
        onPlayButtonClick = {
            vm.startSearch(
                userProfile = me.data!!,
                onRoomCreated = { room ->
                    Toast.makeText(context, room.toString(), Toast.LENGTH_SHORT).show()
                    onNavigateToDuel(room)
                },
                onGameEnd = { room ->
                    Toast.makeText(context, "игра ${room.id} закончена", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                }
            )
        }
    )
}