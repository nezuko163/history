package com.nezuko.domain.model

import java.util.UUID


data class RoomModel(
    val id: String = UUID.randomUUID().toString(),
    val player1: String = "",
    val player2: String = "",
    val status: Status = Status.NONE,
    val questionsList: List<String> = emptyList(),
    val usersAnswers: HashMap<String, HashMap<String, List<Int>>> = hashMapOf(),
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