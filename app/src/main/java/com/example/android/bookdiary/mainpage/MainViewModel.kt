package com.example.android.bookdiary.mainpage

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(val dao: BookDiaryDatabaseDao,
                    application: Application
): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    //--------------------------
    private var _books = dao.getAllBooks()
    val books: LiveData<List<Book>>
        get() = _books
    //--------------------------
    private val _currentList = MutableLiveData<List<Book>>()
    val currentList: LiveData<List<Book>>
        get() = _currentList
    //--------------------------

    private val _authors = dao.getAllAuthors()
    val authors: LiveData<List<String>>
        get() = _authors
    //--------------------------
    private val _navigationToCreate = MutableLiveData<Boolean>()
    val navigationToCreate: LiveData<Boolean?>
        get() = _navigationToCreate
    //--------------------------
    private val _currentBook = MutableLiveData<Book>()
    val currentBook: LiveData<Book?>
        get() = _currentBook
    //--------------------------

    private val _navigateToEditBook = MutableLiveData<Book?>()
    val navigateToEditBook: LiveData<Book?>
        get() = _navigateToEditBook
    //--------------------------


    fun setCurrList(){
        _currentList.postValue(_books.value)
    }
    //Метод, который сообщает об успешном переходе на фрагмент создания элемента
    fun doneNavigating() {
        _navigationToCreate.value = false

    }

    // Метод, который сообщает об успешном переходе на фрагмент редактирования элемента
    fun doneNavigatingTwo(){
        _navigateToEditBook.value = null
    }
    // Метод, возвращающий последнию запись из базы-данных
    private suspend fun getLastFromDatabase(): Book? {
        return withContext(Dispatchers.IO) {
            var num = dao.getLastBook()
            num
        }
    }
    //Метод добавления новой книги
    fun onAdding(){
        uiScope.launch {
            _navigationToCreate.value = true

        }
    }



    //Запрос на изменение книги по id
    fun onEdit(id:Long){
        uiScope.launch{
            _currentBook.value = getById(id)
            _navigateToEditBook.value = _currentBook.value
        }

    }


    //Запрос на удаление по id
    fun onDeleteById(id:Long){
        uiScope.launch {
            deleteById(id)
        }
    }

    //Удаление по id
    private suspend fun deleteById(id:Long){
        withContext(Dispatchers.IO){
            dao.deleteById(id)
        }
    }
    //------------------------------------------------------------------
    fun onFilter(status:String, author:String){
        uiScope.launch {
            val filteredBooksLiveData = filterBuReq(status, author)
            filteredBooksLiveData.observeForever { filteredBooksList ->
                _currentList.postValue(filteredBooksList)
                println(_currentList.value)
            }
        }
    }

    private suspend fun filterBuReq(status: String, author: String):LiveData<List<Book>> {
       return withContext(Dispatchers.IO){
           when {
               status == "All" && author == "All" -> dao.getAllBooks()
               status == "All" && author != "All" -> dao.getBooksByAuthor(author)
               status != "All" && author == "All" -> dao.getBooksByStatus(status)
               else -> dao.getFilteredBooks(status, author)
           }
        }
    }
    //------------------------------------------------------------------
    //Добавление новой книги
    private suspend fun insert(book: Book) {
        withContext(Dispatchers.IO) {
            dao.insert(book)
        }
    }
    //Получить по id книгу
    private suspend fun getById(id:Long):Book{
       return withContext(Dispatchers.IO){
            dao.get(id)

        }
    }
    //Переопределение onCleared()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}