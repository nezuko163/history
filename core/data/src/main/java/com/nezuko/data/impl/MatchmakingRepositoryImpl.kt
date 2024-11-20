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
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.GamesRepository
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MatchmakingRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase,
    private val questionRepository: QuestionRepository,
    private val gamesRepository: GamesRepository,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher,
    @Dispatcher(MyDispatchers.Main) private val MainDispatcher: CoroutineDispatcher
) : MatchmakingRepository {
    private val TAG = "MatchmakingSearchRepositoryImpl"
    private val rooms = db.getReference("rooms")
    private val searchingPlayers = db.getReference("searchingPlayers")

    private val _currentRoom = MutableStateFlow<RoomModel?>(null)
    override val currentRoom = _currentRoom.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    override val isSearching = _isSearching.asStateFlow()

    private val _questions = MutableStateFlow<List<QuestionModel>?>(null)
    override val questions = _questions.asStateFlow()

    private lateinit var roomListener: ChildEventListener

    override suspend fun startSearch(
        user: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit,
    ) {
        _isSearching.update { true }
        searchFreeRooms(user.id)
        Log.i(TAG, "startSearch: start")
        roomListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (_currentRoom.value != null) return
                val room = snapshot.getValue<RoomModel>()
                if (room == null) {
                    throw RuntimeException("onChildAdded: room = null")
                }

                if (room.status == RoomModel.Status.END) return

                if (room.player1 == user.id || room.player2 == user.id) {
                    if (_isSearching.value) {
                        _isSearching.update { false }
                    }
                    onRoomCreated(room)

                    if (_currentRoom.value == null) {
                        _currentRoom.update { room }
                        CoroutineScope(IODispatcher).launch {
                            _questions.update {
                                questionRepository.findQuestionsById(room.questionsList)
                            }
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val room = snapshot.getValue<RoomModel>()
                    ?: throw RuntimeException("onChildChanged: room = null")

                Log.i(TAG, "onChildChanged: room - $room")
                
                gamesRepository.updateGame(room)

                if (_currentRoom.value != null) {
                    if (_currentRoom.value!!.id != room.id) return
                }
                if (room.player1 == user.id || room.player2 == user.id) {
                    if (room.status == RoomModel.Status.END) {
                        onGameEnd(room)
                        _currentRoom.update { null }
                    } else {
                        _currentRoom.update { room }
                    }
                }

                if (_currentRoom.value == null) {
                    if (_questions.value != null) _questions.update { null }
                    if (_currentRoom.value != null) _currentRoom.update { null }
                    if (_isSearching.value) _isSearching.update { false }

                    rooms.ref.removeEventListener(this)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i(TAG, "onChildRemoved: snapshot - $snapshot")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "onChildMoved: snapshot - $snapshot")
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        }

        rooms.ref.addChildEventListener(roomListener)
    }

    private suspend fun searchFreeRooms(playerId: String) =
        withContext(IODispatcher) {
            Log.i(TAG, "searchFreeRooms: start")
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

                            CoroutineScope(coroutineContext).launch(IODispatcher) {
                                key = gamesRepository.createGame(
                                    playerId1 = playerId,
                                    playerId2 = opponentId,
                                    generateQuestions = { questionRepository.generateQuestions() }
                                )
                                    .id
                            }
                            Transaction.success(currentData)
                        }
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (error != null) {
                            continuation.resumeWithException(Exception("Ошибка при поиске игры: ${error.message}"))
                        } else {
                            if (key == null) {
//                                continuation.resumeWithException(RuntimeException("key = null, data - $currentData"))
                            }
                            continuation.resume(key)
                        }
                    }
                })
            }
            Log.i(TAG, "searchFreeRooms: end")
        }


    override suspend fun endGame() = withContext(IODispatcher) {
        Log.i(TAG, "endGame: start")
        suspendCancellableCoroutine { continuation ->
            if (_currentRoom.value == null) {
                continuation.resume(null)
            } else {
                rooms.child(_currentRoom.value!!.id).runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val room = currentData.getValue<RoomModel>() ?: return Transaction.success(
                            currentData
                        )
                        Log.i(TAG, "endGame: doTransaction: 1 room - $room")
                        if (room.status == RoomModel.Status.END) {
                            return Transaction.success(currentData)
                        }
                        var player1Score = 0
                        var player2Score = 0

                        room.usersAnswers.forEach { (userId, answers) ->
                            if (userId == room.player1) {
                                answers.forEach { (questionId, userAnswer) ->
                                    if (userAnswer.correct) {
                                        player1Score += 1
                                    }
                                }
                            } else {
                                answers.forEach { (questionId, userAnswer) ->
                                    if (userAnswer.correct) {
                                        player2Score += 1
                                    }
                                }
                            }
                        }
                        var winner = ""

                        if (player2Score > player1Score) {
                            winner = room.player2
                        } else if (player2Score < player1Score) {
                            winner = room.player1
                        }

                        Log.i(
                            TAG,
                            "endGame: doTransaction: 2 room - ${
                                room.copy(
                                    status = RoomModel.Status.END,
                                    winner = winner
                                )
                            }"
                        )
                        currentData.value =
                            room.copy(status = RoomModel.Status.END, winner = winner)
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (error != null) {
                            continuation.resumeWithException(error.toException())
                        }
                        continuation.resume(null)
                    }

                })
//                rooms.child(_currentRoom.value!!.id).updateChildren(
//                    mapOf("status" to RoomModel.Status.END)
//                )
//                    .addOnSuccessListener {
//                        continuation.resume(null)
//                    }
//                    .addOnFailureListener { e ->
//                        e.printStackTrace()
//                        continuation.resumeWithException(e)
//                    }
//                    .addOnCanceledListener {
//                        continuation.cancel()
//                    }
                Log.i(TAG, "endGame: end")
            }
        }
    }

    override suspend fun onDestroy() {
        if (_currentRoom.value != null) endGame()
        if (::roomListener.isInitialized) rooms.ref.removeEventListener(roomListener)
    }

    override suspend fun stopSearch(
        user: UserProfile,
        onStopSearch: () -> Unit,
    ) {
        Log.i(TAG, "stopSearch: start")
        val coroutineContext = coroutineContext
        withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                searchingPlayers.runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
//                        val players = currentData.value as? HashSet<String> ?: hashSetOf()
                        val players = currentData.value as? ArrayList<String> ?: arrayListOf()


                        if (players.contains(user.id)) {
                            players.remove(user.id)
                            currentData.value = players
                            CoroutineScope(coroutineContext).launch(MainDispatcher) {
                                onStopSearch()
                            }
                        }

                        if (_isSearching.value) {
                            _isSearching.update { false }
                        }

                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (error != null) {
                            continuation.resumeWithException(error.toException())
                            return
                        }
                        continuation.resume(committed)
                    }
                })
            }
        }
        Log.i(TAG, "stopSearch: end")
    }

}
