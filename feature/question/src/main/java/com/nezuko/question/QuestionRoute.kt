package com.nezuko.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuestionRoute(
    modifier: Modifier = Modifier,
    vm: QuestionViewModel = hiltViewModel(),
) {
    val questions by vm.questions.collectAsState()
    if (questions == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "вопросы не загруз", modifier = Modifier.align(Alignment.Center))
        }
    } else {
        QuestionScreen(modifier.fillMaxSize(), question = questions!!.first())
    }
}