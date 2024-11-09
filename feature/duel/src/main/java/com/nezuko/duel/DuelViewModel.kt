package com.nezuko.duel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuelViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val matchmakingRepository: MatchmakingRepository
) : ViewModel() {
    fun endGame() {
        viewModelScope.launch {
            matchmakingRepository.endGame()
        }
    }
}