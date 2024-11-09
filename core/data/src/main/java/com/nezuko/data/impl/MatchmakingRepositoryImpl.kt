package com.nezuko.data.impl

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.getValue
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.MatchmakingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MatchmakingRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher
) : MatchmakingRepository {
    private val TAG = "MatchmakingSearchRepositoryImpl"
    private val rooms = db.getReference("rooms")
    private val searchingPlayers = db.getReference("searchingPlayers")


    private var currentGameId: String? = null

    override suspend fun startSearch(
        user: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit
    ) {
        searchFreeRooms(user.id)

        rooms.ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (currentGameId != null) return
                Log.i(TAG, "onChildAdded: snapshot $snapshot")
                val room = snapshot.getValue<RoomModel>()
                if (room == null) {
                    Log.e(TAG, "onChildAdded: room = null")
                    throw RuntimeException("onChildAdded: room = null")
                }
                if (room.player1 == user.id) {
                    onRoomCreated(room)
                    currentGameId = room.id
                } else if (room.player2 == user.id) {
                    onRoomCreated(room)
                    currentGameId = room.id
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "onChildChanged: snapshot - $snapshot")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val room = snapshot.getValue<RoomModel>()
                if (room == null) {
                    Log.e(TAG, "onChildRemoved: room = null")
                    throw RuntimeException("onChildRemoved: room = null")
                }
                if (room.player1 == user.id) {
                    currentGameId = null
                    onGameEnd(room)
                } else if (room.player2 == user.id) {
                    currentGameId = null
                    onGameEnd(room)
                }
                rooms.ref.removeEventListener(this)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "onChildMoved: snapshot - $snapshot")
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private suspend fun searchFreeRooms(playerId: String) =
        withContext(IODispatcher) {
            suspendCoroutine { continuation ->
                var key: String? = null
                searchingPlayers.runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val players = currentData.value as? ArrayList<String> ?: arrayListOf()

                        return if (players.isEmpty()) {
                            players.add(playerId)
                            currentData.value = players
                            Transaction.success(currentData)
                        } else {
                            val opponentId = players.removeFirst()
                            currentData.value = players
                            key = createRoom(playerId, opponentId)
                            Transaction.success(currentData)
                        }
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        Log.i(TAG, "onComplete: currentData - $currentData")
                        if (error != null) {
                            Log.e(TAG, "onComplete: $error")
                            continuation.resumeWithException(Exception("Ошибка при поиске игры: ${error.message}"))
                        } else {
                            if (key == null) {
                                Log.i(TAG, "onComplete: asd")
//                                continuation.resumeWithException(RuntimeException("key = null, data - $currentData"))
                            }
                            continuation.resume(key)
                        }
                    }
                })
            }
        }


    private fun createRoom(playerId1: String, playerId2: String): String {
        val newRoomRef = rooms.push()

        if (newRoomRef.key == null || newRoomRef.key!!.isEmpty()) throw RuntimeException("newRoomRef.key - null")

        val newRoom = RoomModel(
            id = newRoomRef.key!!,
            player1 = playerId1,
            player2 = playerId2,
            status = RoomModel.Status.GAME
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

        return newRoomRef.key!!
    }

    override suspend fun endGame() = withContext(IODispatcher) {
        val room = getRoom(currentGameId!!)
        suspendCancellableCoroutine { continuation ->
            rooms.child(currentGameId!!).removeValue()
                .addOnSuccessListener {
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

    private suspend fun getRoom(roomId: String) = withContext(IODispatcher) {
        suspendCancellableCoroutine { continuation ->
            rooms.child(roomId).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        continuation.resume(snapshot.getValue<RoomModel>())
                    } else {
                        continuation.resumeWithException(RuntimeException("не существует снапшота"))
                    }
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
