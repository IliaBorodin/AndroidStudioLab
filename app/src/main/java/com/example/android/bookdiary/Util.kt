import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

object UriToByteArrayConverter {
    private const val TAG = "UriToByteArrayConverter"

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    var byteArray: ByteArray? = null
    var inputStream: InputStream? = null
    var outputStream: ByteArrayOutputStream? = null

    try {
        val contentResolver: ContentResolver = context.contentResolver
        inputStream = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len)
            }
            byteArray = outputStream.toByteArray()
        }
    } catch (e: IOException) {
        Log.e(TAG, "Error reading URI to byte array", e)
    } finally {
        try {
            inputStream?.close()
            outputStream?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing streams", e)
        }
    }

    return byteArray
}
}

fun getIndexSpinner(status:String):Int{
    val statuses = listOf("Прочитано", "Читаю", "Буду читать")
    for ((index, item) in statuses.withIndex()) {
        if (item == status) {
            return index
        }
    }
    return 1
}


//Метод для отображения прогресса в чтении
fun updateProgressBar(totalPages: Int, pagesRead: Int, progressBar: ProgressBar):Int {
    // Проверяем, чтобы общее количество страниц было больше нуля, чтобы избежать деления на ноль
    if (totalPages > 0) {
        // Вычисляем процент прочитанных страниц от общего количества страниц
        val progress = (pagesRead.toFloat() / totalPages.toFloat() * 100).toInt()
        // Устанавливаем полученное значение в ProgressBar
        return progress
    } else {
        // Если общее количество страниц равно нулю, то прогресс равен 0
        return 0
    }
}

fun splitAuthors(list: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (item in list) {
        val authors = item.split("\n") // Разделение строки по запятой
        result.addAll(authors.map { it.trim() }) // Добавление каждого автора в новый список
    }
    return result
}


