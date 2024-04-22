package com.example.android.bookdiary.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var bookId: Long = 0L,

    @ColumnInfo(name = "book_status")
    var status: String = "init status",

    @ColumnInfo(name = "name")
    var name: String = "init name",

    @ColumnInfo(name = "author")
    var author: String = "init author",

    @ColumnInfo(name = "genre")
    var genre: String = "init genre",

    @ColumnInfo(name = "page_number")
    var pageNumber: Int = 0,

    @ColumnInfo(name = "page_read")
    var pageRead: Int = 0,

    @ColumnInfo(name = "cover_link")
    var coverPath: ByteArray = ByteArray(0)

)