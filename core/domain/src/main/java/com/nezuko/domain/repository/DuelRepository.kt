package com.nezuko.domain.repository

import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.UserProfile

interface DuelRepository {
    suspend fun answerOnQuestion(
        questionModel: QuestionModel,
        answer: Int,
        user: UserProfile
    )
}