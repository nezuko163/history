package com.nezuko.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val userId = authRepository.currentUserId

    fun onCreate() {
        authRepository.onCreate()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser()
        }
    }

    fun signOut() {
        try {
            authRepository.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при выходе: ${e.message}")
        }
    }

    fun onDestroy() {
        authRepository.onDestroy()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}