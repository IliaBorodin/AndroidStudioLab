package com.example.android.bookdiary.filterpage

import android.app.Application
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.bookdiary.R
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitAuthors

class FilterViewModel(val dao: BookDiaryDatabaseDao,
                      application: Application
): AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope =  CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _authorList = dao.getAllAuthors()
    val authorList: LiveData<List<String>>
        get() = _authorList





    init{
        initLists()
    }

    private fun initLists(){
        uiScope.launch {
           // _authorList.value = getAllAuthors()

        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}