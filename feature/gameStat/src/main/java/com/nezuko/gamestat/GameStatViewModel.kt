package com.nezuko.gamestat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.GamesRepository
import com.nezuko.domain.repository.QuestionRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameStatViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val gamesRepository: GamesRepository,
    private val questionRepository: QuestionRepository
) : ViewModel() {
    val me = userProfileRepository.me

    private val _room = MutableStateFlow<RoomModel?>(null)
    val room = _room.asStateFlow()

    private val _opponent = MutableStateFlow<UserProfile?>(null)
    val opponent = _opponent.asStateFlow()

    fun findOpponent(id: String) {
        viewModelScope.launch {
            _opponent.update { userProfileRepository.getUserProfileById(id) }
        }
    }

    fun findRoom(id: String) {
        viewModelScope.launch {
            _room.update { gamesRepository.findGame(id) }
        }
    }

    fun findMe() {
        viewModelScope.launch {
            userProfileRepository.findMe()
        }
    }

    private val _questions = MutableStateFlow<List<QuestionModel>?>(null)
    val questions = _questions.asStateFlow()

    fun findQuestions() {
        if (_room.value == null) return

        viewModelScope.launch {
            _questions.update {
                questionRepository.findQuestionsById(_room.value!!.questionsList)
            }
        }
    }
}