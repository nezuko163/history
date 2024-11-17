package com.nezuko.duel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.ImageFromInet
import com.nezuko.ui.theme.LightBlue
import com.nezuko.ui.theme.Spacing
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker

@OptIn(ObsoleteCoroutinesApi::class)
@Composable
fun DuelScreen(
    modifier: Modifier = Modifier,
    me: UserProfile,
    opponent: UserProfile,
    onButtonEndGameClick: () -> Unit,
    onGameStart: () -> Unit,
) {
    var searchTime by remember {
        mutableIntStateOf(6)
    }
    LaunchedEffect(Unit) {
        val ticker = ticker(delayMillis = 1000, initialDelayMillis = 0)
        for (event in ticker) {
            if (searchTime > 0) {
                searchTime -= 1 // Уменьшаем значение
            } else {
                ticker.cancel() // Останавливаем таймер, когда доходит до 0
                onGameStart()
                break
            }
        }
    }

    BackHandler {
        onButtonEndGameClick()
    }

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(LightBlue)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Spacing.default.extraLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = opponent.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )

                    Spacer(modifier = Modifier.padding(horizontal = Spacing.default.small))

                    Text(
                        text = opponent.name,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .size(200.dp)
                    .background(Color.White)
                    .border(2.dp, LightBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "Игра начнётся через")

                    Spacer(modifier = Modifier.padding(vertical = Spacing.default.small))

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = searchTime.toString(),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)

            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(Spacing.default.extraLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = me.name,
                        color = LightBlue,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.padding(horizontal = Spacing.default.small))

                    ImageFromInet(
                        modifier = Modifier.size(100.dp),
                        url = me.photoUrl,
                        errorImageResource = com.nezuko.ui.R.drawable.profile
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DuelScreenPreview() {
    DuelScreen(
        me = UserProfile(name = "asd"),
        opponent = UserProfile(name = "гавно"),
        onButtonEndGameClick = {}) {
    }
}