package com.nezuko.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nezuko.ui.theme.LightBlue
import com.nezuko.ui.theme.Spacing
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    onPlayButtonClick: () -> Unit
) {
    Log.i(TAG, "HomeScreen: recomp")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer, заполняющий оставшееся место сверху
        Spacer(modifier = Modifier.weight(1f))

        // Центральный элемент
        Box {
            if (!isSearching) {
                DefaultState(
                    onPlayButtonClick = onPlayButtonClick
                )
            } else {
                SearchingState(
                    onPlayButtonClick = onPlayButtonClick
                )
            }
        }
        // Spacer, заполняющий оставшееся место снизу
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BoxScope.DefaultState(
    modifier: Modifier = Modifier,
    onPlayButtonClick: () -> Unit,
) {
    Button(
        onClick = onPlayButtonClick,
        modifier = Modifier
            .align(Alignment.Center),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightBlue
        ),
    ) {
        Text(
            text = "Играть",
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier
                .padding(Spacing.default.medium)
        )
    }
}

@OptIn(ObsoleteCoroutinesApi::class)
@Composable
private fun BoxScope.SearchingState(
    onPlayButtonClick: () -> Unit
) {
    var searchTime by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(Unit) {
        val ticker = ticker(delayMillis = 1000, initialDelayMillis = 0)
        for (event in ticker) {
            searchTime += 1
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.Center)
    ) {
        Button(
            onClick = onPlayButtonClick,
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
        ) {
            Text(
                text = "Остановить",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(Spacing.default.medium)
            )
        }

//        Spacer(modifier = Modifier.padding(Spacing.default.tiny))
//
        Text(text = "Время поиска: $searchTime секунд")
    }


}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(isSearching = true) {

    }
}