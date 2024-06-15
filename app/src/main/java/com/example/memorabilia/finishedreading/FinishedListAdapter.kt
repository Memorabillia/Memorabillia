package com.example.memorabilia.finishedreading

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.memorabilia.R
import com.example.memorabilia.bookdetail.BookDetailActivity
import com.example.memorabilia.database.FinishedReadingBook
import com.example.memorabilia.database.FinishedReadingBookDao
import com.example.memorabilia.database.WantToReadBook
import com.example.memorabilia.database.WantToReadBookDao
import com.example.memorabilia.search.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinishedListAdapter(private val finishedReadingBookDao: FinishedReadingBookDao) : RecyclerView.Adapter<FinishedListAdapter.ArticleViewHolder>() {
    private var books: List<FinishedReadingBook> = listOf()

    fun setData(books: List<FinishedReadingBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_finished, parent, false)
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

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                view.context as Activity,
                view.findViewById<View>(R.id.profileImageView),
                "bookImageTransition")

            view.context.startActivity(intent, options.toBundle())
        }
        holder.notesEditText.setText(book.notes)
        holder.notesEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val updatedNotes = holder.notesEditText.text.toString()
                book.notes = updatedNotes
                CoroutineScope(Dispatchers.IO).launch {
                    finishedReadingBookDao.updateBookNotes(book.id, updatedNotes)
                }
            }
        }

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        finishedReadingBookDao.deleteBook(book)
                        withContext(Dispatchers.Main) {
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

    fun saveNotesForPosition(position: Int, notes: String) {
        val book = books[position]
        book.notes = notes
        CoroutineScope(Dispatchers.IO).launch {
            finishedReadingBookDao.updateBookNotes(book.id, notes)
        }
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val finishedTextView: TextView = itemView.findViewById(R.id.finishedTextView)
        val notesEditText: EditText = itemView.findViewById(R.id.notesEditText)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }
}