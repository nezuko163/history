package com.nezuko.createquestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {
    fun insertQuestion(
        question: QuestionModel,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            questionRepository.insertQuestion(
                question,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}