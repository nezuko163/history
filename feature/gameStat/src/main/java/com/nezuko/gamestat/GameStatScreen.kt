package com.nezuko.gamestat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.ImageFromInet
import com.nezuko.ui.theme.LightBlue
import com.nezuko.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatScreen(
    modifier: Modifier = Modifier,
    room: RoomModel,
    me: UserProfile,
    questions: List<QuestionModel>,
    opponent: UserProfile,
    onNavigateBack: () -> Unit,
    onMoreClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (room.winner) {
                            me.id -> "Победа"
                            opponent.id -> "Поражение"
                            else -> "Ничья"
                        },
                        color = when (room.winner) {
                            me.id -> Color.Green
                            opponent.id -> Color.Red
                            else -> Color.Black
                        }
                    )
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
                .background(White)
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
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

                Spacer(modifier = Modifier.padding(Spacing.default.medium))

                questions.forEach { question ->

                    val usersAnswers =
                        room.usersAnswers[me.id]?.get(question.id)?.userAnswers ?: emptyList()
                    QuestionCell(
                        modifier = Modifier
                            .background(White)
                            .padding(Spacing.default.medium),
                        question = question,
                        userAnswers = usersAnswers
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionCell(
    question: QuestionModel,
    userAnswers: List<Int>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = { },
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = question.description,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.default.medium)
            )
            QuestionsBox(
                question = question,
                checkedStates = userAnswers,
                showRightAnswer = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QuestionsBox(
    modifier: Modifier = Modifier,
    question: QuestionModel,
    checkedStates: List<Int>,
    showRightAnswer: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.default.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (question.isOneRightAnswer) {
            Column(Modifier.selectableGroup()) {
                question.variants.forEachIndexed { index: Int, variant: String ->
                    val checkedStatesContains = checkedStates.contains(index)
                    val answersContains = question.answers.contains(index)

                    VariantCell(
                        isRight = if (answersContains) checkedStatesContains else {
                            if (checkedStatesContains) false else null
                        },
                        variant = variant,
                        icon = {
                            RadioButton(
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = LightBlue,
                                    disabledSelectedColor = if (answersContains && checkedStatesContains) Green else Red
                                ),
                                enabled = !showRightAnswer,
                                selected = checkedStates.contains(index),
                                onClick = null
                            )
                        },
                        showRightAnswer = showRightAnswer,
                        onClick = {
                        })
                }
            }
        } else {
            Column {
                question.variants.forEachIndexed { index: Int, variant: String ->
                    val checkedStatesContains = checkedStates.contains(index)
                    val answersContains = question.answers.contains(index)
                    VariantCell(
                        isRight = if (answersContains) checkedStatesContains else {
                            if (checkedStatesContains) false else null
                        },
                        variant = variant,
                        icon = {
                            Checkbox(
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LightBlue,
                                    disabledCheckedColor = if (answersContains && checkedStatesContains) Green else Red
                                ),
                                enabled = !showRightAnswer,
                                checked = checkedStates.contains(index),
                                onCheckedChange = null
                            )
                        },
                        showRightAnswer = showRightAnswer,
                        onClick = { })
                }
            }
        }
    }
}

@Composable
private fun VariantCell(
    modifier: Modifier = Modifier,
    isRight: Boolean? = null,
    showRightAnswer: Boolean,
    variant: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.default.small)
            .then(
                if (isRight != null && showRightAnswer) Modifier.border(
                    width = 1.dp,
                    color = if (isRight) Color.Green else Color.Red,
                    shape = RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        onClick = {
            if (!showRightAnswer) onClick() else {
            }
        },
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

            Text(
                text = variant, color = if (showRightAnswer && isRight != null) {
                    if (isRight) Color.Green else Red
                } else Color.Black
            )
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
        questions = listOf(
            QuestionModel(
                id = "26",
                theme = "Дворцовые перевороты",
                description = "Какие личности сыграли ключевую роль в перевороте 1762 года?",
                variants = listOf(
                    "Екатерина II",
                    "Петр III",
                    "Александр Суворов",
                    "Григорий Орлов"
                ),
                answers = listOf(0, 1, 3)
            ),
            QuestionModel(
                id = "27",
                theme = "Правление Петра I",
                description = "Какие реформы Петра I касались военной сферы?",
                variants = listOf(
                    "Создание регулярной армии",
                    "Укрепление флота",
                    "Введение рекрутской повинности",
                    "Реформа налоговой системы"
                ),
                answers = listOf(0, 1, 2)
            ),
            QuestionModel(
                id = "28",
                theme = "Правление Екатерины II",
                description = "Какие из нижеперечисленных действий были частью политики Екатерины II?",
                variants = listOf(
                    "Принятие \"Наказа\" для новых законов",
                    "Запрещение крепостного права",
                    "Открытие Смольного института",
                    "Принятие реформы местного управления"
                ),
                answers = listOf(0, 2, 3)
            ),
            QuestionModel(
                id = "29",
                theme = "Правление Екатерины II",
                description = "Какие из этих войн происходили при Екатерине II?",
                variants = listOf(
                    "Русско-турецкая война 1768–1774",
                    "Северная война",
                    "Русско-польская война",
                    "Гражданская война в России"
                ),
                answers = listOf(0, 2)
            ),
            QuestionModel(
                id = "30",
                theme = "Правление Петра I",
                description = "Какие важнейшие события произошли в правление Петра I?",
                variants = listOf(
                    "Основание Санкт-Петербурга",
                    "Начало Северной войны",
                    "Смерть Ивана Грозного",
                    "Реформа православной церкви"
                ),
                answers = listOf(0, 1)
            ),
            QuestionModel(
                id = "31",
                theme = "Дворцовые перевороты",
                description = "Какие события способствовали свержению Петра III?",
                variants = listOf(
                    "Женитьба на Екатерине II",
                    "Недовольство дворянства",
                    "Невозможность проводить реформы",
                    "Шумные интриги в окружении императора"
                ),
                answers = listOf(1, 2, 3)
            ),
            QuestionModel(
                id = "32",
                theme = "Дворцовые перевороты",
                description = "Кто из следующих личностей был замешан в организации дворцового переворота 1730 года?",
                variants = listOf("Анна Иоанновна", "Милославские", "Голицыны", "Григорий Орлов"),
                answers = listOf(1, 2)
            ),
            QuestionModel(
                id = "33",
                theme = "Дворцовые перевороты",
                description = "Какие факторы привели к длительной борьбе за трон в России в XVIII веке?",
                variants = listOf(
                    "Дворцовые перевороты",
                    "Слабость монархии",
                    "Политика европейских стран",
                    "Нарушение законов наследования"
                ),
                answers = listOf(0, 1, 3)
            ),
            QuestionModel(
                id = "34",
                theme = "Правление Екатерины II",
                description = "Какие реформы Екатерины II оказали влияние на российскую экономику?",
                variants = listOf(
                    "Декреты о промышленности",
                    "Открытие новых торговых путей",
                    "Декреты о крепостных крестьянах",
                    "Пенсии для дворян"
                ),
                answers = listOf(0, 1)
            ),
            QuestionModel(
                id = "35",
                theme = "Правление Петра I",
                description = "Какие реформы Петра I касались образования?",
                variants = listOf(
                    "Открытие новых учебных заведений",
                    "Принятие новых законов для преподавателей",
                    "Налаживание системы народного образования",
                    "Появление первых гимназий"
                ),
                answers = listOf(0, 3)
            )
        ),
        onNavigateBack = { /*TODO*/ }) {
    }
}