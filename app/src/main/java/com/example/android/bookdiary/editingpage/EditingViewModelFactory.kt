package com.example.android.bookdiary.editingpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bookdiary.creaturepage.CreatureViewModel
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabaseDao

class EditingViewModelFactory(
    private val bookId: Long,
    private val dao: BookDiaryDatabaseDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditingViewModel::class.java)) {
            return EditingViewModel(bookId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}