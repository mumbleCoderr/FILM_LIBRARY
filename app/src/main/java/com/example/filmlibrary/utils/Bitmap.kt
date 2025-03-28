package com.example.filmlibrary.utils

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun byteArrayToBitmap(byteArray: ByteArray) =
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

fun uriToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (stream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            byteArrayOutputStream.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}