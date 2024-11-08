package com.nezuko.data.impl

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.domain.model.ResultModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.concurrent.Volatile
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher
) : UserProfileRepository {
    private lateinit var db: FirebaseDatabase

    private lateinit var users: DatabaseReference

    private val _me = MutableStateFlow<ResultModel<UserProfile>>(ResultModel.loading())
    override val me = _me.asStateFlow()

    @Volatile
    private var _uid = ""
    override val uid = _uid

    override fun onStart() {
        db = Firebase.database
        users = db.getReference("users2")
    }

    override fun setUid(uid: String) {
        Log.i(TAG, "setUid: $uid")
        _uid = uid
    }

    override fun setAvatarUrl(url: String) {
        if (_me.value.data == null) {
            Log.e(TAG, "setAvatarUrl: _me = null")
            return
        }
        _me.update { ResultModel.success(it.data!!.copy(photoUrl = url)) }
    }

    override suspend fun findMe() {
        if (_uid.isEmpty()) {
            _me.update { ResultModel.none() }
        } else {
            val user = getUserProfileById(_uid)
            Log.i(TAG, "findMe: $user")
            _me.update { ResultModel.success(user) }
        }
    }

    override suspend fun getUserProfileById(id: String): UserProfile {
        return withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                Log.i(TAG, "getUserProfileById: $id")
                users
                    .child(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            Log.i(TAG, "getUserProfileById: snapshot not exist - $snapshot ")
                            continuation.resumeWithException(RuntimeException("жопа"))
                        }
                        Log.i(TAG, "getUserProfileById: snapshot - $snapshot")
                        val user = snapshot.getValue(UserProfile::class.java)
                        Log.i(TAG, "getUserProfileById: $user")
                        if (user != null) {
                            continuation.resume(user)
                        } else {
                            continuation.resumeWithException(RuntimeException("user not found"))
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "insertUserProfile: $e", e)
                        continuation.resumeWithException(e)
                    }
                    .addOnCanceledListener {
                        Log.i(TAG, "insertUserProfile: cancel")
                        continuation.cancel()
                    }
            }
        }
    }

    override suspend fun updateUserProfileById(userProfile: UserProfile) {
        withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                users
                    .child(userProfile.id)
                    .updateChildren(userProfile.toMap())
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                        Log.e(TAG, "insertUserProfile: $e", e)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                        Log.i(TAG, "insertUserProfile: cancel")
                    }
            }
        }
    }

    override suspend fun insertUserProfile(userProfile: UserProfile) {
        withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                users
                    .child(userProfile.id)
                    .setValue(userProfile)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                        _me.update { ResultModel.success(userProfile) }
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                        Log.e(TAG, "insertUserProfile: $e", e)
                    }
                    .addOnCanceledListener {
                        continuation.cancel()
                        Log.i(TAG, "insertUserProfile: cancel")
                    }
            }
        }
    }

    companion object {
        private const val TAG = "UserProfileRepositoryImpl"
    }
}