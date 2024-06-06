package com.example.memorabilia.bookdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.memorabilia.R
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.currentlyreading.CurrentlyReadingActivity
import com.example.memorabilia.data.DummyData
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.database.FinishedReadingArticle
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.database.CurrentlyReadingBook
import com.example.memorabilia.database.CurrentlyReadingBookDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class BookDetailActivity : AppCompatActivity() {
    private lateinit var currentlyReadingBookDao: CurrentlyReadingBookDao
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        userPreference = UserPreference.getInstance(this.dataStore)

        currentlyReadingBookDao = BookDatabase.getDatabase(this).currentlyReadingBookDao()

        val article = intent.getParcelableExtra<Article>("article")
        val currentlyReadingBook = intent.getParcelableExtra<CurrentlyReadingBook>("book")

        if (article != null) {
            displayArticleDetails(article)
        } else if (currentlyReadingBook != null) {
            displayCurrentlyReadingBookDetails(currentlyReadingBook)
        } else {
            val dummyArticle = DummyData.getDummyArticles().first()
            displayArticleDetails(dummyArticle)
        }




        val manageBookButton = findViewById<Button>(R.id.manageBookButton)
        manageBookButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Manage Book")
                .setItems(arrayOf("Currently Reading", "Want to Read", "Finished Reading")) { _, which ->
                    when (which) {
                        0 -> addToCurrentlyReading(article)
                        //1 -> addToWantToRead(article)
                        //2 -> addToFinishedReading(article)
                    }
                }
                .show()
        }
    }

    private fun addToCurrentlyReading(article: Article?) {
        article?.let {
            val userId = getCurrentUserId() // Mendapatkan ID pengguna yang sedang login
            val currentlyReadingBook = CurrentlyReadingBook(0, userId, it.title, it.author,
                it.urlToImage, 0)
            CoroutineScope(Dispatchers.IO).launch {
                currentlyReadingBookDao.insertCurrentlyReadingBook(currentlyReadingBook)
            }
        }
    }

    private fun getCurrentUserId(): String {
        val userModel = runBlocking {
            userPreference.getSession().firstOrNull()
        }
        return userModel?.email ?: ""
    }
    private fun displayArticleDetails(article: Article) {
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val articleImageView = findViewById<ImageView>(R.id.bookImageView)

        titleTextView.text = article.title
        authorTextView.text = article.author
        contentTextView.text = article.content
        Glide.with(this)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
            .error(R.drawable.ic_profile) // Image to display if loading fails
            .into(articleImageView)
    }

    private fun displayCurrentlyReadingBookDetails(book: CurrentlyReadingBook) {
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val articleImageView = findViewById<ImageView>(R.id.bookImageView)

        titleTextView?.text = book.title
        authorTextView?.text = book.author
        Glide.with(this)
            .load(book.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_profile)
            .into(articleImageView)
    }







}


