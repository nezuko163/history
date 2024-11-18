package com.nezuko.gamestat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.ImageFromInet
import com.nezuko.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatScreen(
    modifier: Modifier = Modifier,
    room: RoomModel,
    me: UserProfile,
    opponent: UserProfile,
    onNavigateBack: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (room.winner == me.id) {
                        Text(
                            text = "Победа",
                            color = Color.Green,
                        )
                    } else {
                        Text(
                            text = "Поражение",
                            color = Color.Red,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = onMoreClick) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.padding(vertical = Spacing.default.extraLarge * 2))

            Row {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = me.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )
                    Text(text = me.name)
                }

                Text(text = "vs", modifier = Modifier.align(Alignment.CenterVertically))

                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = opponent.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )
                    Text(text = opponent.name)
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameStatScreenPreview() {
    GameStatScreen(
        room = RoomModel(winner = "qwe"),
        me = UserProfile(id = "asd", name = "крутышка"),
        opponent = UserProfile(id = "qwe", name = "нуб"),
        onNavigateBack = { /*TODO*/ }) {
    }
}