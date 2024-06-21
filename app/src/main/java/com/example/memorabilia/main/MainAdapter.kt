package com.example.memorabilia.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memorabilia.R
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.bookdetail.BookDetailActivity

class MainAdapter : RecyclerView.Adapter<MainAdapter.BookViewHolder>() {
    private var books: List<Book> = listOf()

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_main, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
        Glide.with(holder.itemView.context)
            .load(book.cover)
            .placeholder(R.drawable.logo)
            .error(R.drawable.ic_launcher_background)
            .into(holder.bookImageView)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                view.context as Activity,
                view.findViewById<View>(R.id.bookImageView), // View yang ingin di-share
                "bookImageTransition"
            )

            view.context.startActivity(intent, options.toBundle())
        }

    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
        }
    }
}
