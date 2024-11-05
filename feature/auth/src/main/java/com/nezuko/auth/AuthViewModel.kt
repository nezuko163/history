package com.nezuko.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.signInWithEmailAndPassword(email, password)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при входе: ${e.message}")
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.createUserWithEmailAndPassword(email, password)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при входе: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}