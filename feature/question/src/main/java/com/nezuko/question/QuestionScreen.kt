package com.nezuko.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.nezuko.domain.model.QuestionModel
import com.nezuko.ui.theme.Spacing

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    question: QuestionModel,
    onAnswerButtonClick: (List<Int>) -> Unit = {}
) {
    val checkedStates = remember { mutableStateListOf<Int>() }

    val onCheckedChange: (index: Int, isChecked: Boolean) -> Unit =
        if (question.isOneRightAnswer) { index, isCheked ->
            checkedStates.clear()
            checkedStates.add(index)
        } else { index, isChecked ->
            if (isChecked) {
                checkedStates.add(index)
            } else {
                checkedStates.remove(index)
            }
        }

    Box(modifier) {
        QuestionsBox(
            modifier = Modifier.align(Alignment.Center),
            question = question,
            checkedStates = checkedStates,
            onCheckedChange = onCheckedChange
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
    onCheckedChange: (index: Int, isChecked: Boolean) -> Unit
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
                        description = variant,
                        icon = {
                            RadioButton(selected = checkedStates.contains(index), onClick = null)
                        },
                        onClick = {
                            onCheckedChange(index, !checkedStates.contains(index))
                        })
                }
            }

        } else {
            val childCheckedStates = remember {
                mutableStateListOf<Boolean>().apply { repeat(question.variants.size) { add(false) } }
            }

            Column {
                childCheckedStates.forEachIndexed { index: Int, isChecked: Boolean ->
                    VariantCell(
                        isRight = question.answers.contains(index),
                        description = question.variants[index],
                        icon = {
                            Checkbox(checked = childCheckedStates[index], onCheckedChange = null)
                        },
                        onClick = { childCheckedStates[index] = !childCheckedStates[index] })
                }
            }
        }
    }
}

@Composable
private fun VariantCell(
    modifier: Modifier = Modifier,
    isRight: Boolean,
    description: String,
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

            Text(text = description)
        }
    }
}

@Preview
@Composable
private fun VariantCellPreview() {
    VariantCell(
        modifier = Modifier.fillMaxWidth(),
        isRight = true,
        description = "Юля я люблю тебя",
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
        variants = listOf("фвфвы", "мне больно", "я не хочу тебя терять"),
        answers = listOf(2)
    )
    QuestionScreen(question = question)
}

@Preview
@Composable
private fun QuestionBoxWithTwoAnswerPreview() {
    val question = QuestionModel(
        description = "я не знаю",
        variants = listOf("фвфвы", "мне больно", "я не хочу тебя терять"),
        answers = listOf(1, 2)
    )
    QuestionScreen(question = question)
}