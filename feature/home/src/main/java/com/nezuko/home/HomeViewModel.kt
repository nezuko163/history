package com.nezuko.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.QuestionRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val matchmakingRepository: MatchmakingRepository,
    private val userProfileRepository: UserProfileRepository,
    private val questionRepository: QuestionRepository
) : ViewModel() {
    val me = userProfileRepository.me
    val isSearching = matchmakingRepository.isSearching

    fun startSearch(
        userProfile: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit
    ) {
        viewModelScope.launch {
            matchmakingRepository.startSearch(
                user = userProfile,
                onRoomCreated = onRoomCreated,
                onGameEnd = onGameEnd
            )
        }
    }

    fun stopSearch(
        userProfile: UserProfile,
        onSearchStopped: () -> Unit
    ) {
        viewModelScope.launch {
            matchmakingRepository.stopSearch(
                user = userProfile,
                onStopSearch = onSearchStopped
            )
        }
    }

    fun insertQuestion(questionModel: QuestionModel) {
        viewModelScope.launch {
            questionRepository.insertQuestion(questionModel)
        }
    }
}