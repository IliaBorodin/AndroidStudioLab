package com.example.android.bookdiary.creaturepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatureViewModel(
    val dao: BookDiaryDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean?>
        get() = _navigateToMain
    private var _coverLink = MutableLiveData<ByteArray>()
    val coverLink: LiveData<ByteArray>
        get() = _coverLink

    fun changeLink(link: ByteArray) {
        _coverLink.value = link
    }


    fun doneNavigating() {
        _navigateToMain.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //Фактически метод отвечает за добавление книги в список книг
    fun onSetBook(
        bookName: String,
        authorName: String,
        genreBook: String,
        pageCount: String,
        pageRead: String,
        status: String,
        linkCover: ByteArray,
    ) {
        uiScope.launch {
            withContext(Dispatchers.IO) {

                val book = Book() ?: return@withContext
                book.status = status
                book.name = bookName
                book.author = authorName
                book.genre = genreBook
                book.pageNumber = Integer.parseInt(pageCount)
                if(status =="Read"){
                    book.pageRead = Integer.parseInt(pageCount)
                }else book.pageRead = Integer.parseInt(pageRead)
                book.coverPath = linkCover
                dao.insert(book)
            }
            _navigateToMain.value = true
        }

    }

}

