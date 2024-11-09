package com.nezuko.domain.repository

import com.nezuko.domain.model.ResultModel
import com.nezuko.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface UserProfileRepository {
    val me: StateFlow<ResultModel<UserProfile>>
    val uid: String

    fun setUid(uid: String)
    fun setAvatarUrl(url: String)

    suspend fun getUserProfileById(id: String): UserProfile
    suspend fun updateUserProfileById(userProfile: UserProfile)
    suspend fun insertUserProfile(userProfile: UserProfile)
    suspend fun findMe()
}