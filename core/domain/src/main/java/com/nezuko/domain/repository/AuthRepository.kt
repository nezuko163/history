package com.nezuko.domain.repository

import com.nezuko.domain.model.ResultModel
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUserId: StateFlow<ResultModel<String>>

    fun onCreate()
    fun onDestroy()
    suspend fun getCurrentUser(): String?
    fun signOut()
    suspend fun signInWithEmailAndPassword(email: String, password: String): String?
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): String
}