package com.nezuko.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.AuthRepository
import com.nezuko.domain.repository.RemoteStorageRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val remoteStorageRepository: RemoteStorageRepository
) : ViewModel() {
    val me = userProfileRepository.me

    fun onCreate() {
        authRepository.onCreate()
        userProfileRepository.onStart()
        remoteStorageRepository.onCreate()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()
            Log.i(TAG, "getCurrentUser: $uid")
            if (uid != null) {
                userProfileRepository.setUid(uid)
                Log.i(TAG, "getCurrentUser: ${userProfileRepository.uid}")
            }
            userProfileRepository.findMe()
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