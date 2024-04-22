package com.example.android.bookdiary.mainpage

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bookdiary.R
import com.example.android.bookdiary.database.Book
import com.example.android.bookdiary.database.BookDiaryDatabaseDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import updateProgressBar


class BookDiaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val bookImage: ImageView = itemView.findViewById(R.id.coverImageView)
    private val titleBook: TextView = itemView.findViewById(R.id.titleTextView)
    private val authorBook: TextView = itemView.findViewById(R.id.authorTextView)
    private val genreBook: TextView = itemView.findViewById(R.id.genreTextView)
    private val pagesCount: TextView = itemView.findViewById(R.id.pagesTextView)
    private val bookStatus: TextView = itemView.findViewById(R.id.statusTextView)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
     val deleteBtn: FloatingActionButton = itemView.findViewById(R.id.deleteButton)
     val editBtn : FloatingActionButton = itemView.findViewById(R.id.editButton)


    fun bind(item: Book){
        try {
            if(item.coverPath.size == 0){
                bookImage.setImageResource(R.drawable.default_book)
            }
            else {
                val bitmap = BitmapFactory.decodeByteArray(item.coverPath, 0, item.coverPath.size)
                bookImage.setImageBitmap(bitmap)

            }
        }catch (e: Exception){
            bookImage.setImageResource(R.drawable.default_book)
        }


        titleBook.text = item.name
        authorBook.text = item.author.toString() //Сделать также метод для нескольких авторов
        genreBook.text = item.genre
        pagesCount.text = item.pageNumber.toString()
        bookStatus.text = item.status
        progressBar.progress = updateProgressBar(item.pageNumber, item.pageRead, progressBar)

    }



    companion object {
        fun from(parent:ViewGroup): BookDiaryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_book, parent, false)
            return  BookDiaryViewHolder(view)
        }
    }
}

class BookDiaryAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<BookDiaryViewHolder>(){
    interface OnItemClickListener {
        fun onDeleteClick(position: Long)
        fun onEditClick(position: Long)
    }



    var data = listOf<Book>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BookDiaryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.deleteBtn.setOnClickListener {
            listener.onDeleteClick(item.bookId)
        }

        holder.editBtn.setOnClickListener {
            listener.onEditClick(item.bookId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookDiaryViewHolder {
        return BookDiaryViewHolder.from(parent)
    }

}