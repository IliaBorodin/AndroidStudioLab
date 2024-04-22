package com.example.android.bookdiary.filterpage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import com.example.android.bookdiary.editingpage.EditingViewModel

class FilterViewModelFactory(
    private val dao: BookDiaryDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel( dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}