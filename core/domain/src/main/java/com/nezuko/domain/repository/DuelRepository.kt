package com.nezuko.domain.repository

import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile

interface DuelRepository {
    suspend fun answerOnQuestion(
        room: RoomModel,
        questionNumber: Int,
        answer: List<Int>,
        user: UserProfile
    )
}