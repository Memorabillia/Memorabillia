package com.example.memorabilia.wanttoread

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.memorabilia.R
import com.example.memorabilia.bookdetail.BookDetailActivity
import com.example.memorabilia.database.CurrentlyReadingBook
import com.example.memorabilia.database.CurrentlyReadingBookDao
import com.example.memorabilia.database.FinishedReadingBook
import com.example.memorabilia.database.WantToReadBook
import com.example.memorabilia.database.WantToReadBookDao
import com.example.memorabilia.search.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WantToReadAdapter(private val wantToReadBookDao: WantToReadBookDao,
                        private val currentlyReadingBookDao: CurrentlyReadingBookDao) : RecyclerView.Adapter<WantToReadAdapter.ArticleViewHolder>() {
    private var books: List<WantToReadBook> = listOf()

    fun setData(books: List<WantToReadBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_want_to_read, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        Glide.with(holder.itemView.context)
            .load(book.cover)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(holder.articleImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)
            it.context.startActivity(intent)
        }
        holder.moveButton.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Move Book")
                .setMessage("Are you sure you want to move this book to the 'Currently Reading' list?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // Create a new FinishedReadingBook object with the same details as the CurrentlyReadingBook object
                        val currentlyReadingBook = CurrentlyReadingBook(0, book.userId, book.title, book.author, book.cover, 0)

                        // Insert the FinishedReadingBook object into the FinishedReadingBookDao
                        currentlyReadingBookDao.insertCurrentlyReadingBook(currentlyReadingBook)

                        // Delete the CurrentlyReadingBook object from the CurrentlyReadingBookDao
                        wantToReadBookDao.deleteBook(book)

                        withContext(Dispatchers.Main) {
                            // Remove the book from the list and notify the adapter
                            books = books.filter { it.id != book.id }
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        wantToReadBookDao.deleteBook(book)
                        withContext(Dispatchers.Main) {
                            // Remove the book from the list and notify the adapter
                            books = books.filter { it.id != book.id }
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val moveButton: Button = itemView.findViewById(R.id.moveButton)
    }
}