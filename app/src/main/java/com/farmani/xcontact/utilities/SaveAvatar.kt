package com.farmani.xcontact.utilities

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

object SaveAvatar {
    fun saveAvatar(context: Context, data: Uri): String? {
        try {
            val avatarName = UUID.randomUUID().toString() + "." + uriToExtension(context, data)
            context.openFileOutput(avatarName, Context.MODE_PRIVATE).write(uriToByte(context, data))
            return avatarName
        } catch (e: java.lang.Exception) {
           return null
        }
    }

    fun uriToByte(context: Context, data: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(data)!!
        val outputStream = ByteArrayOutputStream()
        outputStream.write(inputStream.readBytes())
        inputStream.close()
        return outputStream.toByteArray()
    }

    fun uriToExtension(context: Context, data: Uri): String {
        val contentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()

        return mime.getExtensionFromMimeType(contentResolver.getType(data)) ?: ""
    }
}