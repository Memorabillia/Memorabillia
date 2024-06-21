package com.example.memorabilia.search

import android.app.Activity
import android.content.Context
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

class SearchAdapter(private val context: Context) : RecyclerView.Adapter<SearchAdapter.BookViewHolder>() {
    private var books: List<Book> = listOf()

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                view.context as Activity,
                view.findViewById<View>(R.id.bookImageView),
                "bookImageTransition"
            )

            view.context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int = books.size

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)

        fun bind(book: Book?) {
            if (book != null) {
                titleTextView.text = book.title
                authorTextView.text = book.author
                Glide.with(context)
                    .load(book.cover)
                    .into(bookImageView)
            }
        }
    }
}
