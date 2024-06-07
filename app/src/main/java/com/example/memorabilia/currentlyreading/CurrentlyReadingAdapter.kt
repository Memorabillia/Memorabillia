package com.example.memorabilia.currentlyreading

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
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
        Glide.with(holder.itemView.context)
            .load(book.urlToImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(holder.articleImageView)
        holder.progressSeekBar.progress = book.progress
        holder.progressTextView.text = "${book.progress}%"

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, BookDetailActivity::class.java)
            intent.putExtra("book", book)
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
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val progressSeekBar: SeekBar = itemView.findViewById(R.id.progressSeekBar)
        val progressTextView: TextView = itemView.findViewById(R.id.progressTextView)
    }
}

