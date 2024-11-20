package com.nezuko.domain.repository

import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel

interface GamesRepository {
    suspend fun findGame(id: String): RoomModel
    suspend fun createGame(
        playerId1: String,
        playerId2: String,
        generateQuestions: suspend () -> List<QuestionModel>,
        theme: String = "ALL"
    ): RoomModel

    fun updateGame(room: RoomModel)

    suspend fun analyzeAnswers(
        room: RoomModel
    )
}