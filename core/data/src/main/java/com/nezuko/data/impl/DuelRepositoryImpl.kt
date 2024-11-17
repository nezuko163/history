package com.nezuko.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.DuelRepository
import javax.inject.Inject

class DuelRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase
) : DuelRepository {
    private val rooms = db.getReference("rooms")
    private val answers = rooms.child("answers")

    override suspend fun answerOnQuestion(
        room: RoomModel,
        questionNumber: Int,
        answer: List<Int>,
        user: UserProfile
    ) {

    }
}