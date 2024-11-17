package com.nezuko.question

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nezuko.domain.model.QuestionModel
import com.nezuko.ui.theme.Spacing

private const val TAG = "QuestionScreen"

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    question: QuestionModel,
    onBackHandler: () -> Unit,
    onAnswerButtonClick: (List<Int>) -> Unit
) {
    BackHandler {
        Log.i(TAG, "QuestionScreen: asd")
        onBackHandler()
    }

    val checkedStates = remember { mutableStateListOf<Int>() }

    val onCheckedChange: (index: Int, isChecked: Boolean) -> Unit

    if (question.isOneRightAnswer) {
        onCheckedChange = { index, isChecked ->
            Log.i(TAG, "QuestionScreen: one right: index - $index, isChecked - $isChecked")
            checkedStates.clear()
            checkedStates.add(index)
        }
    } else {
        onCheckedChange = { index, isChecked ->
            Log.i(TAG, "QuestionScreen: more right: index - $index, isChecked - $isChecked")
            if (isChecked) {
                checkedStates.add(index)
            } else {
                checkedStates.remove(index)
            }
            Log.i(TAG, "QuestionScreen: list - $checkedStates")
        }
    }

    Box(modifier.fillMaxSize()) {
        Text(
            text = question.description,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = Spacing.default.extraLarge * 3),
            fontSize = 20.sp
        )

        QuestionsBox(
            modifier = Modifier.align(Alignment.Center),
            question = question,
            checkedStates = checkedStates,
            myOnCheckedChange = onCheckedChange
        )

        Button(
            onClick = { onAnswerButtonClick(checkedStates) },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Ответить")
        }
    }
}

@Composable
private fun QuestionsBox(
    modifier: Modifier = Modifier,
    question: QuestionModel,
    checkedStates: List<Int>,
    myOnCheckedChange: (index: Int, isChecked: Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (question.isOneRightAnswer) {
            Column(Modifier.selectableGroup()) {
                question.variants.forEachIndexed { index: Int, variant: String ->
                    VariantCell(
                        isRight = question.answers.contains(index),
                        variant = variant,
                        icon = {
                            RadioButton(selected = checkedStates.contains(index), onClick = null)
                        },
                        onClick = {
                            myOnCheckedChange(index, !checkedStates.contains(index))
                        })
                }
            }

        } else {
            Column {
                question.variants.forEachIndexed { index: Int, variant: String ->
                    VariantCell(
                        isRight = question.answers.contains(index),
                        variant = variant,
                        icon = {
                            Checkbox(
                                checked = checkedStates.contains(index),
                                onCheckedChange = null
                            )
                        },
                        onClick = { myOnCheckedChange(index, !checkedStates.contains(index)) })
                }
            }
        }
    }
}

@Composable
private fun VariantCell(
    modifier: Modifier = Modifier,
    isRight: Boolean,
    variant: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.default.small),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(Spacing.default.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Spacer(modifier = Modifier.padding(horizontal = Spacing.default.small))

            Text(text = variant)
        }
    }
}

@Preview
@Composable
private fun VariantCellPreview() {
    VariantCell(
        modifier = Modifier.fillMaxWidth(),
        isRight = true,
        variant = "домик",
        icon = {
            Icon(imageVector = Icons.Default.Home, contentDescription = null)
        }) {

    }
}

@Preview
@Composable
private fun QuestionBoxWithOneAnswerPreview() {
    val question = QuestionModel(
        description = "я не знаю",
        variants = listOf("фвфвы", "пися попа", "эщкере"),
        answers = listOf(2)
    )
    QuestionScreen(question = question , onBackHandler = {}) {}
}

@Preview
@Composable
private fun QuestionBoxWithTwoAnswerPreview() {
    val question = QuestionModel(
        description = "я не знаю",
        variants = listOf("фвфвы", "пися попа", "эщкере"),
        answers = listOf(1, 2)
    )
    QuestionScreen(question = question, onBackHandler = {}) {}
}