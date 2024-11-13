package com.nezuko.duel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuelViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val matchmakingRepository: MatchmakingRepository
) : ViewModel() {
    val me = userProfileRepository.me
    private val _opponent = MutableStateFlow<UserProfile?>(null)
    val opponent = _opponent.asStateFlow()
    val currentRoom = matchmakingRepository.currentRoom

    fun endGame() {
        viewModelScope.launch {
            matchmakingRepository.endGame()
        }
    }

    fun findOpponent(opponentId: String) {
        viewModelScope.launch {
            _opponent.update { userProfileRepository.getUserProfileById(opponentId) }
        }
    }
}