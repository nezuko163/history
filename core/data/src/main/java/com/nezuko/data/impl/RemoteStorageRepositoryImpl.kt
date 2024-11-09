package com.nezuko.data.impl

import android.content.Context
import android.net.Uri
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.data.source.RemoteSource
import com.nezuko.domain.repository.RemoteStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class RemoteStorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteSource: RemoteSource,
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher
) : RemoteStorageRepository {
    override suspend fun uploadPhotoUrl(uri: Uri) = remoteSource.uploadFileToYandexS3(uri)

    override suspend fun downloadPhotoUrl(imageName: String): String {
        return withContext(IODispatcher) {
            suspendCancellableCoroutine { continuation ->
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                    continuation.resumeWithException(e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "RemoteStorageRepositoryImpl"
    }
}