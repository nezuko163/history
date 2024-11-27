package com.nezuko.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel

private const val TAG = "HomeRoute"

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDuel: (room: RoomModel) -> Unit,
) {
    val me by vm.me.collectAsState()
    val isSearching by vm.isSearching.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val questionsWithMultipleAnswers = listOf(
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
        )
//        val semaphore = Semaphore(10)
//        coroutineScope {
//            questionsWithMultipleAnswers.forEach { question ->
//                launch {
//                    semaphore.withPermit {
//                        vm.insertQuestion(question)
//                    }
//                }
//            }
//        }
    }

    val startSearch = {
        vm.startSearch(
            userProfile = me!!,
            onRoomCreated = { room ->
                Toast.makeText(context, room.toString(), Toast.LENGTH_SHORT).show()
                onNavigateToDuel(room)
            },
            onGameEnd = { room ->
//                Toast.makeText(context, "игра ${room.id} закончена", Toast.LENGTH_SHORT).show()
            }
        )
    }
    val stopSearch = {
        vm.stopSearch(
            userProfile = me!!,
            onSearchStopped = {
                Toast.makeText(context, "поиск закончен", Toast.LENGTH_SHORT).show()
            }
        )
    }

    val onPlayButtonClick: () -> Unit = if (isSearching) {
        stopSearch
    } else {
        startSearch
    }

    HomeScreen(
        modifier = modifier,
        isSearching = isSearching,
        onPlayButtonClick = onPlayButtonClick
    )
}