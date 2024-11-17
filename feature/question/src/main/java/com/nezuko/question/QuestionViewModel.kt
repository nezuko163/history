package com.nezuko.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.MatchmakingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val matchmakingRepository: MatchmakingRepository
) : ViewModel() {
    val questions = matchmakingRepository.questions

    fun endGame() {
        viewModelScope.launch {
            matchmakingRepository.endGame()
        }
    }
}