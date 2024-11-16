package com.nezuko.domain.model

data class QuestionModel(
    val id: String = "",
    val theme: String = "",
    val description: String = "",
    val variants: List<String> = emptyList(),
    val answers: List<Int> = emptyList(),
) {
    val isOneRightAnswer: Boolean
        get() = answers.size == 1
    fun setId(id: String) = copy(id = id)
    fun setTheme(theme: String = "ALL") = copy(theme = theme)
}