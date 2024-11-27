package com.nezuko.gamehistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.GamesRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject

@HiltViewModel
class GameHistoryViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val gamesRepository: GamesRepository
) : ViewModel() {
    private val TAG = "GameHistoryViewModel"
    private val _games = MutableStateFlow<List<RoomModel>?>(null)
    val games = _games.asStateFlow()

    fun getUserGames(userId: String) {
        viewModelScope.launch {
            Log.i(TAG, "getUserGames: start")
            _games.update { gamesRepository.findUserGames(userId) }
            Log.i(TAG, "getUserGames: end")
        }
    }

    fun clearHistory() { _games.update { null } }

    private val _users = MutableStateFlow<HashMap<String, UserProfile>?>(null)
    val users = _users.asStateFlow()

    fun searchUsers(users: List<String>) {
        val semaphore = Semaphore(10)
        viewModelScope.launch {
            _users.update {
                val deferredResults = users.map { userId ->
                    async {
                        semaphore.withPermit {
                            userProfileRepository.getUserProfileById(userId)
                        }
                    }
                }

                val results = deferredResults.awaitAll()
                users.zip(results).toMap(HashMap())
            }
        }
    }
}