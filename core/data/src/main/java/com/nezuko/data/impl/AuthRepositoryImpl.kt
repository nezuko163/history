package com.nezuko.data.impl

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.ResultModel
import com.nezuko.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher,
    private val auth: FirebaseAuth
) : AuthRepository {

    private lateinit var authStateListener: AuthStateListener
    private lateinit var tokenIdListener: FirebaseAuth.IdTokenListener

    private val _currentUserId = MutableStateFlow<ResultModel<String>>(ResultModel.loading())
    override val currentUserId = _currentUserId.asStateFlow()

    private var isLoaded = false

    override fun onCreate() {
        authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.i(TAG, "onCreate: user.id - ${user?.uid}")
            Log.i(TAG, "onCreate: _currentUserId - ${_currentUserId.value}")
            if (user == null) {
                Log.i(TAG, "onCreate: user = null")
                _currentUserId.update { ResultModel.none() }
            } else {
                Log.i(TAG, "onCreate: user != null")
                if (_currentUserId.value.data != user.uid) {
                    _currentUserId.update { ResultModel.success(user.uid) }
                }
            }
        }
//        tokenIdListener = FirebaseAuth.IdTokenListener { firebaseAuth: FirebaseAuth ->
//            val user = firebaseAuth.currentUser
//            Log.i(TAG, "idTokedListener: $user")
//            if (user == null) {
//                _currentUserId.update { ResultModel.none() }
//                signOut()
//            }
//        }
//        auth.addIdTokenListener(tokenIdListener)
        auth.addAuthStateListener(authStateListener)
    }

    override fun onDestroy() {
        if (::authStateListener.isInitialized) {
            auth.removeAuthStateListener(authStateListener)
        }
        if (::tokenIdListener.isInitialized) {
            auth.removeIdTokenListener(tokenIdListener)
        }
    }


    override suspend fun getCurrentUser(): String? {
        isLoaded = true
        try {
            val currentUser = auth.currentUser
            Log.i(TAG, "getCurrentUser: 1")
            if (currentUser == null) {
                _currentUserId.update { ResultModel.none() }
                Log.i(TAG, "getCurrentUser: 2")
                return null
            }
            return withContext(IODispatcher) {
                Log.i(TAG, "getCurrentUser: 3")
                isLoaded = true
                suspendCancellableCoroutine<String> { continuation ->
                    currentUser.reload()
                        .addOnSuccessListener {
                            Log.i(TAG, "getCurrentUser: success")
                            continuation.resume(currentUser.uid)
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
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "getCurrentUser: $e", e)
            _currentUserId.update { ResultModel.none() }
            return null
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): String? {
        return withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { firebaseAuth ->
                        if (firebaseAuth.user != null) {
                            continuation.resume(firebaseAuth.user!!.uid)
                        } else {
                            continuation.resumeWithException(RuntimeException("user null"))
                        }
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

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): String {
        return withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { auth ->
                        if (auth.user != null) {
                            continuation.resume(auth.user!!.uid)
                        } else {
                            continuation.resumeWithException(RuntimeException("не получилось создать"))
                        }
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

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}