package com.nezuko.createquestion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nezuko.ui.theme.Gray
import com.nezuko.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    modifier: Modifier = Modifier,
    description: String,
    optionsOfAnswer: List<String>,
    answers: MutableList<Int>,
    onCreateQuestionButtonClick: () -> Unit,
    onOptionAdd: () -> Unit,
    onArrowBackClick: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onOptionRemoved: (Int) -> Unit,
    onOptionChanged: (index: Int, value: String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .clickable { focusManager.clearFocus() },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Создать вопрос")
                },
                navigationIcon = {
                    IconButton(onClick = onArrowBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(Spacing.default.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = description,
                onValueChange = onDescriptionChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.default.medium),
                placeholder = {
                    Text(text = "Введите вопрос")
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    disabledContainerColor = White
                )
            )

            Spacer(modifier = Modifier.padding(Spacing.default.tiny))

            Text(
                text = "Добавить вариант ответа",
                modifier = Modifier
                    .clickable { onOptionAdd() },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
            )
            Spacer(modifier = Modifier.padding(Spacing.default.small))
            Text(
                text = "Выберите правильный ответы, поставив на них галочку",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(Spacing.default.large))

            Column {
                optionsOfAnswer.forEachIndexed { index, option ->
                    val isChecked = answers.contains(index)
                    VariantCell(
                        modifier = Modifier.padding(Spacing.default.small),
                        text = option,
                        isChecked = isChecked,
                        onClick = {
                            if (isChecked) answers.remove(index) else answers.add(index)
                        },
                        onRemoveClick = {
                            onOptionRemoved(index)
                        },
                        onTextChange = { value -> onOptionChanged(index, value) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onCreateQuestionButtonClick,
                modifier = Modifier.padding(bottom = Spacing.default.large)
            ) {
                Text(text = "Создать")
            }
        }
    }
}

@Composable
fun VariantCell(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) } // Состояние фокуса
    val focusRequester = remember { FocusRequester() }

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Spacing.default.small)
        ) {
            Checkbox(
                modifier = Modifier.padding(Spacing.default.small),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                ),
                checked = isChecked,
                onCheckedChange = null
            )

            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .focusable()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = TextStyle(
                    color = if (isFocused) Black else Gray,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                ),
                decorationBox = { innerTextField ->
                    if (!isFocused) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Введите текст",
                                color = Black.copy(0.6f)
                            )

                        } else {
                            Text(text = text)
                        }
                    } else {
                        innerTextField()
                    }
                }
            )

            IconButton(onClick = onRemoveClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
            }

        }
    }
}

@Preview
@Composable
private fun VariantCellPreview() {
    VariantCell(
        text = "asd",
        isChecked = true,
        onClick = {},
        onRemoveClick = { },
        onTextChange = {})
}

@Preview
@Composable
private fun CreateQuestionScreenPreview() {
    var description by remember { mutableStateOf("пися попа") }
    val optionsOfAnswer = remember { mutableStateListOf("какашка", "писи", "Жопы") }
    val answers = remember { mutableStateListOf<Int>(1) }
    var deletedIndex by remember { mutableIntStateOf(-1) }

    Box {
        CreateQuestionScreen(
            description = description,
            optionsOfAnswer = optionsOfAnswer,
            answers = answers,
            onCreateQuestionButtonClick = {
            },
            onArrowBackClick = {},
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

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {

            Text(text = "answers - ${answers.toList()}")
            Text(text = "deleted - $deletedIndex")
            Text(text = "options - ${optionsOfAnswer.toList()}")
        }
    }
}