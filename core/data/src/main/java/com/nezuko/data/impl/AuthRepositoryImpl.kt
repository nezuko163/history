package com.nezuko.data.impl

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.ResultModel
import com.nezuko.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher
) : AuthRepository {
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener

    private val _currentUserId = MutableStateFlow<ResultModel<String>>(ResultModel.loading())
    override val currentUserId = _currentUserId.asStateFlow()

    private var isLoaded = false

    override fun onCreate() {
        auth = Firebase.auth
        authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.i(TAG, "onCreate: user.id - ${user?.uid}")
            Log.i(TAG, "onCreate: _currentUserId - ${_currentUserId.value}")
            if (user == null) {
                Log.i(TAG, "onCreate: user = null")
                if (_currentUserId.value.data != null) {
                    signOut()
                }
                _currentUserId.update { ResultModel.none() }
            } else {
                Log.i(TAG, "onCreate: user != null")
                if (_currentUserId.value.data != user.uid) {
                    _currentUserId.update { ResultModel.success(user.uid) }
                }
            }
        }
        auth.addAuthStateListener(authStateListener)
    }

    override fun onDestroy() {
        if (::auth.isInitialized) {
            if (::authStateListener.isInitialized) {
                auth.removeAuthStateListener(authStateListener)
            }
        } else {
            Log.i(TAG, "onDestroy: auth isn't init")
            return

        }
    }

    override suspend fun getCurrentUser() {
        isLoaded = true
        try {
            val currentUser = auth.currentUser
            Log.i(TAG, "getCurrentUser: 1")
            if (currentUser == null) {
                _currentUserId.update { ResultModel.none() }
                Log.i(TAG, "getCurrentUser: 2")
                return
            }

            coroutineScope {
                launch(IODispatcher) {
                    Log.i(TAG, "getCurrentUser: 3")
                    isLoaded = true
                    suspendCancellableCoroutine { continuation ->
                        currentUser.reload()
                            .addOnSuccessListener {
                                Log.i(TAG, "getCurrentUser: success")
                                continuation.resume(Unit)
                            }
                            .addOnFailureListener { exception ->
                                Log.i(TAG, "getCurrentUser: $exception")
                                Toast.makeText(context, "Ошибка при входе", Toast.LENGTH_SHORT)
                                    .show()
                                continuation.resumeWithException(exception)
                            }.addOnCanceledListener {
                                Log.i(TAG, "getCurrentUser: cancel")
                                Toast.makeText(context, "Вход отменён", Toast.LENGTH_SHORT).show()
                                continuation.cancel()
                            }
                    }
                }
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "getCurrentUser: $e", e)
            _currentUserId.update { ResultModel.none() }
        }
    }

    override fun signOut() {
        if (!::auth.isInitialized) {
            Log.i(TAG, "signOut: auth isn't init")
            return
        }

        auth.signOut()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        if (!::auth.isInitialized) {
            Log.i(TAG, "signInWithEmailAndPassword: auth isn't init")
            return
        }

        coroutineScope {
            launch(IODispatcher) {
                suspendCancellableCoroutine { continuation ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Ошибка при входе", Toast.LENGTH_SHORT).show()
                            continuation.resumeWithException(exception)
                        }.addOnCanceledListener {
                            Toast.makeText(context, "Вход отменён", Toast.LENGTH_SHORT).show()
                            continuation.cancel()
                        }
                }
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        Log.i(TAG, "createUserWithEmailAndPassword: $email, $password")
        if (!::auth.isInitialized) {
            Log.i(TAG, "createUserWithEmailAndPassword: auth isn't init")
            return
        }

        coroutineScope {
            launch(IODispatcher) {
                suspendCancellableCoroutine { continuation ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Ошибка при входе", Toast.LENGTH_SHORT).show()
                            continuation.resumeWithException(exception)
                        }
                        .addOnCanceledListener {
                            Toast.makeText(context, "Вход отменён", Toast.LENGTH_SHORT).show()
                            continuation.cancel()
                        }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}