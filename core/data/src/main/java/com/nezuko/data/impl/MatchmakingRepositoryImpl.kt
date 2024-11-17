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


    override suspend fun startSearch(
        user: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit,

        ) {
        _isSearching.update { true }
        searchFreeRooms(user.id)

        rooms.ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (_currentRoom.value != null) return
                Log.i(TAG, "onChildAdded: snapshot $snapshot")
                val room = snapshot.getValue<RoomModel>()
                if (room == null) {
                    Log.e(TAG, "onChildAdded: room = null")
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
                Log.i(TAG, "onChildChanged: snapshot - $snapshot")

                val room = snapshot.getValue<RoomModel>()
                if (room == null) {
                    Log.e(TAG, "onChildChanged: room = null")
                    throw RuntimeException("onChildChanged: room = null")
                }

                Log.i(TAG, "onChildChanged: cur room - ${_currentRoom.value}")

                if (_currentRoom.value != null) {
                    if (_currentRoom.value!!.id != room.id) return
                }

                Log.i(TAG, "onChildChanged: room - $room")

                if (room.player1 == user.id || room.player2 == user.id) {
                    Log.i(TAG, "onChildChanged: asd")
                    onGameEnd(room)
                    _currentRoom.update { null }
                }

                if (_questions.value != null) _questions.update { null }
                if (_currentRoom.value != null) _currentRoom.update { null }
                if (_isSearching.value) _isSearching.update { false }

                rooms.ref.removeEventListener(this)
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

                            CoroutineScope(coroutineContext).launch(IODispatcher) {
                                key = createRoom(playerId, opponentId)
                            }
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

    // TODO: очень сильно надо выделить логику генериации вопросов отдельно
    private suspend fun createRoom(
        playerId1: String,
        playerId2: String,
        theme: String = "ALL"
    ): String {
        val newRoomRef = rooms.push()

        if (newRoomRef.key == null || newRoomRef.key!!.isEmpty()) throw RuntimeException("newRoomRef.key - null")

        Log.i(TAG, "createRoom: start")
        val questions: List<QuestionModel> = questionRepository.generateQuestions()

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

        return newRoomRef.key!!
    }


    override suspend fun endGame() = withContext(IODispatcher) {
        suspendCancellableCoroutine { continuation ->
            if (_currentRoom.value == null) {
                continuation.resumeWithException(RuntimeException("игра не началась"))
            } else {
                rooms.child(_currentRoom.value!!.id).updateChildren(
                    _currentRoom.value!!.copy(status = RoomModel.Status.END).toMap()
                )
                    .addOnSuccessListener {
                        continuation.resume(null)
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

    override suspend fun stopSearch(
        user: UserProfile,
        onStopSearch: () -> Unit,
    ) {
        val coroutineContext = coroutineContext
        withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                searchingPlayers.runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
//                        val players = currentData.value as? HashSet<String> ?: hashSetOf()
                        val players = currentData.value as? ArrayList<String> ?: arrayListOf()

                        Log.i(TAG, "doTransaction: $players")

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
                        Log.i(TAG, "stopSearch - onComplete: data - $currentData")
                        continuation.resume(committed)
                    }
                })
            }
        }
    }

}
