package com.nezuko.duel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nezuko.domain.model.UserProfile

@Composable
fun DuelScreen(
    modifier: Modifier = Modifier,
    me: UserProfile,
    opponent: UserProfile,
    onButtonEndGameClick: () -> Unit,
    onQuestionButtonClick: () -> Unit,
) {
    BackHandler {

    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer, заполняющий оставшееся место сверху
        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onQuestionButtonClick) {
            Text(text = "Перейти к вопросам")
        }

        // Центральный элемент
        Button(onClick = onButtonEndGameClick) {
            Text(text = "Закончить игру")
        }
        // Элементы под центральным
        Column(
            modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Элемент 1")
            Text(text = "Элемент 2")
            Text(text = "Элемент 3")
        }

        // Spacer, заполняющий оставшееся место снизу
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun DuelScreenPreview() {
    DuelScreen(me = UserProfile(), opponent = UserProfile(), onButtonEndGameClick = {}) {

    }
}