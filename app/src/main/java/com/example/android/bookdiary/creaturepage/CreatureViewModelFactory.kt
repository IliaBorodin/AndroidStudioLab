package com.example.android.bookdiary.creaturepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bookdiary.database.BookDiaryDatabaseDao

class CreatureViewModelFactory(
    private val dao: BookDiaryDatabaseDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatureViewModel::class.java)) {
            return CreatureViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}