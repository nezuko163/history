package com.nezuko.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun getFileFromContentUri(context: Context, contentUri: Uri): File? {
    // Получаем имя файла (например, "image.jpg") из Uri или задаем временное имя
    val fileName = contentUri.lastPathSegment ?: "tempFile"
    // Создаем временный файл в кэше приложения (можно использовать context.filesDir для постоянного хранения)
    val tempFile = File(context.cacheDir, fileName)

    return try {
        // Открываем InputStream для чтения данных из Uri
        val inputStream: InputStream? = context.contentResolver.openInputStream(contentUri)
        val outputStream = FileOutputStream(tempFile)

        // Копируем данные из InputStream во временный файл
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        tempFile // Возвращаем созданный временный файл
    } catch (e: Exception) {
        e.printStackTrace()
        null // Вернем null в случае ошибки
    }
}

fun getInputStreamFromUri(context: Context, uri: Uri): InputStream? {
    return try {
        context.contentResolver.openInputStream(uri)
    } catch (e: Exception) {
        e.printStackTrace()
        null // Вернем null в случае ошибки
    }
}