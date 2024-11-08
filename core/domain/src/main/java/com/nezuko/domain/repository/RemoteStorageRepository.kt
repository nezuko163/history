package com.nezuko.domain.repository

import android.net.Uri

interface RemoteStorageRepository {
    fun onCreate()
    fun onDestroy()

    suspend fun uploadPhotoUrl(uri: Uri): String
    suspend fun downloadPhotoUrl(imageName: String): String
}