package com.nezuko.domain.model

import java.util.UUID


data class RoomModel(
    val id: String = UUID.randomUUID().toString(),
    val player1: String = "",
    val player2: String = "",
    val status: Status = Status.NONE
) {
    enum class Status {
        NONE, WAITING, GAME, END
    }
}