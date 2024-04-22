package com.example.android.bookdiary.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDiaryDatabase : RoomDatabase() {

    abstract fun getBookDiaryDatabaseDao(): BookDiaryDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: BookDiaryDatabase? = null

        fun getInstance(context: Context): BookDiaryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        BookDiaryDatabase::class.java, "books_db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}