package com.example.android.bookdiary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDiaryDatabaseDao {

    @Insert
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM book_table WHERE id = :key")
    fun get(key: Long): Book

    @Query("DELETE FROM book_table")
    fun clear()

    @Query("DELETE FROM book_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM book_table ORDER BY id DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM book_table ORDER BY id DESC LIMIT 1")
    fun getLastBook(): Book?

    @Query("SELECT DISTINCT author FROM book_table")
    fun getAllAuthors(): LiveData<List<String>>

    @Query("SELECT * FROM book_table WHERE book_status =:statusParam")
    fun getBooksByStatus(statusParam: String):LiveData<List<Book>>

    @Query("SELECT * FROM book_table WHERE author LIKE '%' || :authorParam || '%'")
    fun getBooksByAuthor(authorParam: String):LiveData<List<Book>>

    @Query("SELECT * FROM book_table WHERE author LIKE '%' || :authorParam || '%' AND book_status =:statusParam")
    fun getFilteredBooks(statusParam: String, authorParam: String):LiveData<List<Book>>





}