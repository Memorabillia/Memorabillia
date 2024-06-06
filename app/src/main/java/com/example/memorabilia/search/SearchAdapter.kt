package com.example.memorabilia.search

import com.example.memorabilia.R

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.bookdetail.BookDetailActivity


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ArticleViewHolder>() {
    private var articles: List<Article> = listOf()

    fun setData(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title
        holder.authorTextView.text = article.author
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(holder.articleImageView)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, BookDetailActivity::class.java)
            intent.putExtra("article", article)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.bookImageView)
    }
}