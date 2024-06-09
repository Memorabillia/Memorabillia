package com.example.memorabilia.main


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.memorabilia.R
import com.example.memorabilia.api.response.Article
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
        if (book != null) {
            holder.bind(book)
            Glide.with(holder.itemView.context)
                .load(book.cover)
                .into(holder.bookImageView)


//        holder.itemView.setOnClickListener {
//            val intent = Intent(it.context, BookDetailActivity::class.java)
//            intent.putExtra("book", book)
//            it.context.startActivity(intent)
//        }
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