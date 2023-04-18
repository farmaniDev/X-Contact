package com.farmani.xcontact.utilities

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


object AvatarUtil {
    @Synchronized
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

    @Synchronized
    fun loadAvatar(context: Context, name: String): File {
        val avatar = File(context.cacheDir, name)
        if (avatar.exists())
            return avatar

        val avatarBytes = context.openFileInput(name).readBytes()
        val fileOut = FileOutputStream(avatar)
        fileOut.write(avatarBytes)

        return avatar
    }
}