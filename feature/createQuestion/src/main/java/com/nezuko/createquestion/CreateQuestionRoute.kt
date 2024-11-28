package com.nezuko.createquestion

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.domain.model.QuestionModel

@Composable
fun CreateQuestionRoute(
    onNavigateBack: () -> Unit,
    vm: CreateQuestionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var description by remember { mutableStateOf("") }
    val optionsOfAnswer = remember { mutableStateListOf<String>() }
    val answers = remember { mutableStateListOf<Int>() }
    var deletedIndex by remember { mutableIntStateOf(-1) }

    val context = LocalContext.current

    CreateQuestionScreen(
        description = description,
        optionsOfAnswer = optionsOfAnswer,
        answers = answers,
        onCreateQuestionButtonClick = {
            vm.insertQuestion(
                QuestionModel(
                    description = description,
                    variants = optionsOfAnswer,
                    answers = answers
                ),
                onSuccess = {
                    Toast.makeText(context, "вопрос сохранён", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                onFailure = {
                    Toast.makeText(context, "ошибка при сохранении", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                }
            )
        },
        onArrowBackClick = onNavigateBack,
        onOptionAdd = {
            optionsOfAnswer.add("")
        },
        onDescriptionChanged = { description = it },
        onOptionRemoved = { index ->
            deletedIndex = index
            optionsOfAnswer.removeAt(deletedIndex)
            if (answers.contains(deletedIndex)) answers.remove(deletedIndex)
            answers.forEachIndexed { i, value ->
                if (value > deletedIndex) answers[i] -= 1
            }
        },
        onOptionChanged = { index, value ->
            optionsOfAnswer[index] = value
        }
    )
}