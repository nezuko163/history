package com.nezuko.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.DuelRepository
import javax.inject.Inject

class DuelRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase
) : DuelRepository {
    private val rooms = db.getReference("rooms")
    private val answers = rooms.child("answers")

    override suspend fun answerOnQuestion(
        questionModel: QuestionModel,
        answer: Int,
        user: UserProfile
    ) {
        answers
    }
}