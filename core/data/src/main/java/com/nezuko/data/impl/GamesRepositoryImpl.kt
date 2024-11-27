package com.nezuko.data.impl

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.repository.GamesRepository
import com.nezuko.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GamesRepositoryImpl @Inject constructor(
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher,
    private val db: FirebaseDatabase,
    private val questionRepository: QuestionRepository
) : GamesRepository {
    private val rooms = db.getReference("rooms")

    private val TAG = "GamesRepositoryImpl"

    private val cachedGames = HashMap<String, RoomModel>()

    override suspend fun findGame(gameId: String): RoomModel {
        if (cachedGames.containsKey(gameId)) {
            val room = cachedGames[gameId]!!
            if (room.usersAnswers.isNotEmpty()) {
                return room
            }
        }

        return withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                rooms.child(gameId).get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            Log.e(TAG, "findGame: no game with id = $gameId")
                            continuation.resumeWithException(RuntimeException("findGame: no game with id = $gameId"))
                        }

                        val room = snapshot.getValue(RoomModel::class.java)
                        if (room == null) {
                            Log.i(TAG, "findGame: room != null")
                            Log.e(TAG, "findGame: no game with id = $gameId")
                            continuation.resumeWithException(RuntimeException("findGame: no game with id = $gameId"))
                            return@addOnSuccessListener
                        }
                        cachedGames[room.id] = room
                        continuation.resume(room)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        continuation.resumeWithException(e)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                    }
            }
        }
    }

    override suspend fun createGame(
        playerId1: String,
        playerId2: String,
        generateQuestions: suspend () -> List<QuestionModel>,
        theme: String
    ): RoomModel {
        val newRoomRef = rooms.push()

        if (newRoomRef.key == null || newRoomRef.key!!.isEmpty()) throw RuntimeException("newRoomRef.key - null")

        Log.i(TAG, "createRoom: start")
        val questions: List<QuestionModel> = generateQuestions()

        Log.i(TAG, "createRoom: end")


        val newRoom = RoomModel(
            id = newRoomRef.key!!,
            player1 = playerId1,
            player2 = playerId2,
            status = RoomModel.Status.GAME,
            questionsList = questions.map { it.id }
        )

        newRoomRef.setValue(newRoom)
            .addOnSuccessListener {
                Log.i(TAG, "createRoom: ураааа создана")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCanceledListener {
                Log.e(TAG, "createRoom: cancel")
            }

        return newRoom
    }

    override suspend fun findUserGames(userId: String): List<RoomModel> {
        return coroutineScope {
            val first = async { getPlayerGames(userId, true) }
            val second = async { getPlayerGames(userId, false) }

            first.await() + second.await()
        }
    }

    private suspend fun getPlayerGames(userId: String, isFirst: Boolean) =
        withContext(IODispatcher) {
            suspendCancellableCoroutine<List<RoomModel>> { continuation ->
                rooms.orderByChild("player" + if (isFirst) "1" else "2").equalTo(userId).get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            continuation.resume(emptyList())
                            return@addOnSuccessListener
                        }

                        val rooms = snapshot.children.mapNotNull {
                            val room = it.getValue<RoomModel>()
                            if (room != null) {
                                cachedGames[room.id] = room
                            }
                            room
                        }
                        continuation.resume(rooms)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
                    .addOnCanceledListener {
                        Log.e(TAG, "createRoom: cancel")
                    }
            }
        }

    override fun updateGame(room: RoomModel) {
        cachedGames[room.id] = room
    }
}