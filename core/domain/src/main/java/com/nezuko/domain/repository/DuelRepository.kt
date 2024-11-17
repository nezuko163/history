package com.nezuko.domain.repository

import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile

interface DuelRepository {
    suspend fun answerOnQuestion(
        room: RoomModel,
        question: QuestionModel,
        answers: List<Int>,
        user: UserProfile
    )
}