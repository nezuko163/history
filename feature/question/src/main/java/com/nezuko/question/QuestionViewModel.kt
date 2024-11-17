package com.nezuko.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.repository.DuelRepository
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val matchmakingRepository: MatchmakingRepository,
    private val userProfileRepository: UserProfileRepository,
    private val duelRepository: DuelRepository
) : ViewModel() {
    private val me = userProfileRepository.me.value.data!!
    private val room = matchmakingRepository.currentRoom.value!!

    val questions = matchmakingRepository.questions

    fun endGame() {
        viewModelScope.launch {
            matchmakingRepository.endGame()
        }
    }

    fun answerOnQuestion(
        question: QuestionModel,
        answers: List<Int>,
    ) {
        viewModelScope.launch {
            duelRepository.answerOnQuestion(
                room = room,
                question = question,
                answers = answers,
                user = me
            )
        }
    }
}