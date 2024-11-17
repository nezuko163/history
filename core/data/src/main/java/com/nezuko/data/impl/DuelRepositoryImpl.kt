package com.nezuko.data.impl

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.DuelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class DuelRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher
) : DuelRepository {
    private val rooms = db.getReference("rooms")
    private val TAG = "DuelRepositoryImpl"

    override suspend fun answerOnQuestion(
        room: RoomModel,
        question: QuestionModel,
        answers: List<Int>,
        user: UserProfile
    ) {
        withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                rooms
                    .child(room.id)
                    .child("usersAnswers")
                    .child(user.id)
                    .updateChildren(mapOf(question.id to answers))
                    .addOnSuccessListener {
                        Log.i(TAG, "answerOnQuestion: success")
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "answerOnQuestion: error", e)
                        e.printStackTrace()
                    }
                    .addOnCanceledListener {
                        Log.i(TAG, "answerOnQuestion: cancel")
                        continuation.cancel()
                    }
            }
        }

    }
}