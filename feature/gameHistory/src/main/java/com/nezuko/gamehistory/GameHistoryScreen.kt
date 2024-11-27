package com.nezuko.gamehistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.ImageFromInet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHistoryScreen(
    modifier: Modifier = Modifier,
    user: UserProfile,
    games: List<RoomModel>,
    users: HashMap<String, UserProfile>,
    onGameClick: (room: RoomModel) -> Unit,
    onArrowClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = user.name) },
                navigationIcon = {
                    IconButton(onClick = onArrowClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            games.forEach { game ->
                GameCard(
                    room = game,
                    player1 = user,
                    player2 = if (game.player1 == user.id) users[game.player2]!! else users[game.player1]!!,
                    onGameClick = { onGameClick(game) }
                )
            }
        }
    }

}

@Composable
private fun GameCard(
    modifier: Modifier = Modifier,
    room: RoomModel,
    player1: UserProfile,
    player2: UserProfile,
    onGameClick: () -> Unit
) {
    ElevatedCard(
        onClick = onGameClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = when (room.winner) {
                    player1.id -> "Победа"
                    player2.id -> "Поражение"
                    else -> "Ничья"
                },
                color = when (room.winner) {
                    player1.id -> Color.Green
                    player2.id -> Color.Red
                    else -> Color.Black
                }
            )
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = player1.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )
                    Text(text = player1.name)
                }

                Text(text = "vs", modifier = Modifier.align(Alignment.CenterVertically))

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = player2.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )
                    Text(text = player2.name)
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameCardPreview() {
    val p1 = UserProfile(id = "12", name = "сися")
    val p2 = UserProfile(id = "2", name = "мёдик")
    GameCard(
        room = RoomModel(player1 = p1.id, winner = p1.id),
        player1 = p1,
        player2 = p2,
        onGameClick = {})
}