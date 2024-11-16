package com.nezuko.domain.repository

import com.nezuko.domain.model.QuestionModel

interface QuestionRepository {
    suspend fun getAllQuestions(): ArrayList<QuestionModel>

    suspend fun insertQuestion(questionModel: QuestionModel) : QuestionModel

    suspend fun generateQuestions(theme: String = "ALL", count: Int = 10): List<QuestionModel>

    suspend fun findQuestionById(id: String): QuestionModel

    suspend fun findQuestionsById(listId: List<String>): List<QuestionModel>
}