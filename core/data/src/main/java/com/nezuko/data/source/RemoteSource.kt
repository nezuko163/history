package com.nezuko.data.source

import android.content.Context
import android.net.Uri
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.UploadOptions
import com.amazonaws.services.s3.AmazonS3Client
import com.nezuko.data.di.Dispatcher
import com.nezuko.data.di.MyDispatchers
import com.nezuko.data.getInputStreamFromUri
import com.nezuko.data.source.amazon.ApiRoute.BUCKET_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteSource @Inject constructor(
    @Dispatcher(MyDispatchers.IO) private val IODispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val s3Client: AmazonS3Client,
    private val transferUtility: TransferUtility,
) {
    private val TAG = "RemoteSource"

    suspend fun uploadFileToYandexS3(filePath: Uri): String {
        val objectKey = "usersProfileImages/${filePath.lastPathSegment}"
        return withContext(IODispatcher) {
            // Асинхронно загружаем файл с помощью корутин
            suspendCancellableCoroutine { continuation ->
                val uploadObserver: TransferObserver =
                    transferUtility.upload(
                        objectKey,
                        getInputStreamFromUri(context, filePath),
                        UploadOptions(
                            UploadOptions.builder()
                                .bucket(BUCKET_NAME)
                        )
                    )
                uploadObserver.setTransferListener(object :
                    com.amazonaws.mobileconnectors.s3.transferutility.TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                        if (state == TransferState.COMPLETED) {
                            val fileUrl = "https://$BUCKET_NAME.storage.yandexcloud.net/$objectKey"
                            uploadObserver.cleanTransferListener()
                            continuation.resume(fileUrl)
                        } else if (state == TransferState.FAILED) {
                            uploadObserver.cleanTransferListener()
                            continuation.resumeWithException(Exception("Ошибка загрузки файла."))
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        uploadObserver.cleanTransferListener()
                        continuation.resumeWithException(ex ?: Exception("Неизвестная ошибка"))
                    }
                })

                // Отмена операции, если корутина отменена
                continuation.invokeOnCancellation {
                    uploadObserver.cleanTransferListener()
                }
            }
        }
    }
}