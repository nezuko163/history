package com.nezuko.domain.model

import java.util.UUID


data class RoomModel(
    val id: String = UUID.randomUUID().toString(),
    val player1: String = "",
    val player2: String = "",
    val status: Status = Status.NONE,
    val questionsList: List<String> = emptyList(),
    val usersAnswers: HashMap<String, HashMap<String, UserAnswer>> = hashMapOf(),
    val winner: String = ""
) {
    enum class Status {
        NONE, WAITING, GAME, END
    }

    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "player1" to player1,
        "player2" to player2,
        "status" to status,
        "questionsList" to questionsList,
        "usersAnswers" to usersAnswers
    )
}

data class UserAnswer(
    val rightAnswers: List<Int> = emptyList(),
    val userAnswers: List<Int> = emptyList(),
    val correct: Boolean = false
) {
    fun toMap() = mapOf(
        "rightAnswers" to rightAnswers,
        "userAnswers" to userAnswers,
        "correct" to correct
    )
}