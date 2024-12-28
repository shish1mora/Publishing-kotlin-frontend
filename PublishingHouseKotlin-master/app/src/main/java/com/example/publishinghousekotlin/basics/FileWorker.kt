package com.example.publishinghousekotlin.basics


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Base64

/**
 * Класс для работы с файлами
 * @author Денис
 * @since 1.0.0
 */
class FileWorker() {

    /**
     * Метод получечения файла по его Uri
     * @param[uri] путь до файла
     * @param[context] контекст Activity или приложения
     * @return При успешном получении - File, в противном случае - null
     */
     fun uriToFile(uri: Uri, context: Context): File? {

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
        val filePath: String? = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        filePath?.let { return File(it) }
        return null
    }

    /**
     * Метод получения Bitmap из Base64String
     * @param[image] строка Base64String
     * @return Объект Bitmap
     */
    fun getBitmap(image: String): Bitmap{
        val bytes = Base64.getDecoder().decode(image)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * Метод получения File из Base64String
     * @param[base64String] строка Base64String
     * @return Объект File
     */
    fun fromBase64ToFile(base64String: String): File{
        val decodedBytes = Base64.getDecoder().decode(base64String)
        val byteArrayInputStream = ByteArrayInputStream(decodedBytes)

        val tempFile = File.createTempFile("photo", ".png")
        val fileOutputStream = FileOutputStream(tempFile)

        fileOutputStream.write(byteArrayInputStream.readBytes())
        fileOutputStream.close()
        byteArrayInputStream.close()

        return tempFile
    }

}