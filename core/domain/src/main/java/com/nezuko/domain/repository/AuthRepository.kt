package com.nezuko.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUserId: StateFlow<String?>

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