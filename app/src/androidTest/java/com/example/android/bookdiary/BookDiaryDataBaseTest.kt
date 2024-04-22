package com.example.android.bookdiary

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabase
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var numberDao: BookDiaryDatabaseDao
    private lateinit var db: BookDiaryDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, BookDiaryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        numberDao = db.getBookDiaryDatabaseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNumber() {
        val book = Book()
        numberDao.insert(book)
        val last_book = numberDao.getLastBook()
        TestCase.assertEquals(last_book?.pageNumber, 0)
    }


}