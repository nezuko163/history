package com.nezuko.question

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "QuestionRoute"

@Composable
fun QuestionRoute(
    onNavigateToGameStat: (roomId: String) -> Unit,
    modifier: Modifier = Modifier,
    vm: QuestionViewModel = hiltViewModel(),
) {
    val room by vm.room.collectAsState()
    var roomId by remember { mutableStateOf(room?.id) }
    val vmQuestions by vm.questions.collectAsState()
    var questions by remember { mutableStateOf(vmQuestions) }
    var numberOfCurrentQuestion by remember { mutableIntStateOf(0) }
    val checkedStates = remember { mutableStateListOf<Int>() }

    LaunchedEffect(room) {
        if (room != null) {
            roomId = room!!.id
        }
    }

    LaunchedEffect(vmQuestions) {
        if (vmQuestions != null) questions = vmQuestions
    }

    if (questions == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "вопросы не загруз", modifier = Modifier.align(Alignment.Center))
        }
    } else {
        QuestionScreen(
            modifier = modifier.fillMaxSize(),
            question = questions!![numberOfCurrentQuestion],
            checkedStates = checkedStates,
            numberOfQuestion = numberOfCurrentQuestion + 1,
            countOfQuestions = questions!!.size,
            onAnswerButtonClick = { answers ->
                vm.answerOnQuestion(
                    question = questions!![numberOfCurrentQuestion],
                    answers = answers
                )
            },
            onTimeEnd = {
                checkedStates.clear()
                if (numberOfCurrentQuestion == questions!!.size - 1) {
                    vm.endGame()
                    onNavigateToGameStat(roomId!!)
                } else {
                    numberOfCurrentQuestion++
                }
                Log.i(TAG, "QuestionRoute: questions - {$questions!!}")
                Log.i(TAG, "QuestionRoute: number - $numberOfCurrentQuestion")
            },
            onBackHandler = {
                Log.i(TAG, "QuestionRoute: on back")
            }
        )
    }
}