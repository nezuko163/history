package com.nezuko.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.AuthRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
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

    fun createUserWithEmailAndPassword(email: String, name: String, password: String) {
        viewModelScope.launch {
            try {
                val uid = authRepository.createUserWithEmailAndPassword(email, password)
                if (uid.isEmpty()) throw RuntimeException("юзер не создан")
                Log.i(TAG, "createUserWithEmailAndPassword: uid - $uid")
                val user = UserProfile(
                    id = uid,
                    name = name,
                    email = email
                )
                Log.i(TAG, "createUserWithEmailAndPassword: user - $uid")
                userProfileRepository.insertUserProfile(
                    user
                )
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при входе: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}