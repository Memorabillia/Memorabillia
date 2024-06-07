package com.example.memorabilia.currentlyreading

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.memorabilia.R
import com.example.memorabilia.bookdetail.BookDetailActivity
import com.example.memorabilia.database.CurrentlyReadingBook
import com.example.memorabilia.database.CurrentlyReadingBookDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentlyReadingAdapter(private val currentlyReadingBookDao: CurrentlyReadingBookDao) : RecyclerView.Adapter<CurrentlyReadingAdapter.ArticleViewHolder>() {
    private var books: List<CurrentlyReadingBook> = listOf()

    fun setData(books: List<CurrentlyReadingBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_currently, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.contentTextView.text = book.description
        Glide.with(holder.itemView.context)
            .load(book.urlToImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(holder.articleImageView)
        holder.progressSeekBar.progress = book.progress
        holder.progressTextView.text = "${book.progress}%"

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)
            intent.putExtra("content", book.description) // Add this line
            it.context.startActivity(intent)
        }


        holder.progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    book.progress = progress
                    CoroutineScope(Dispatchers.IO).launch {
                        currentlyReadingBookDao.updateBookProgress(book.id, progress)
                    }
                    holder.progressTextView.text = "$progress%"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No action needed here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No action needed here
            }
        })
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        currentlyReadingBookDao.deleteBook(book)
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
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val progressSeekBar: SeekBar = itemView.findViewById(R.id.progressSeekBar)
        val progressTextView: TextView = itemView.findViewById(R.id.progressTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

    }
}

