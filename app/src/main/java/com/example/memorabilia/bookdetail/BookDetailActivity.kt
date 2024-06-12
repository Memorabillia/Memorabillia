package com.example.memorabilia.bookdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.memorabilia.R
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.currentlyreading.CurrentlyReadingActivity
import com.example.memorabilia.data.DummyData
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.database.CurrentlyReadingBook
import com.example.memorabilia.database.CurrentlyReadingBookDao
import com.example.memorabilia.database.FinishedReadingBook
import com.example.memorabilia.database.FinishedReadingBookDao
import com.example.memorabilia.database.WantToReadBook
import com.example.memorabilia.database.WantToReadBookDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class BookDetailActivity : AppCompatActivity() {
    private lateinit var wantToReadBookDao: WantToReadBookDao
    private lateinit var currentlyReadingBookDao: CurrentlyReadingBookDao
    private lateinit var finishedReadingBookDao: FinishedReadingBookDao
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        userPreference = UserPreference.getInstance(this.dataStore)
        currentlyReadingBookDao = BookDatabase.getDatabase(this).currentlyReadingBookDao()
        wantToReadBookDao = BookDatabase.getDatabase(this).wantToReadBookDao()
        finishedReadingBookDao = BookDatabase.getDatabase(this).finishedReadingBookDao()

        val book = intent.getSerializableExtra("book") as? Book
        if (book != null) {
            displayBookDetails(book)

        val manageBookButton = findViewById<Button>(R.id.manageBookButton)
        manageBookButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Manage Book")
                .setItems(arrayOf("Currently Reading", "Want to Read", "Finished Reading")) { _, which ->
                    when (which) {
                        0 -> addToCurrentlyReading(book)
                        1 -> addToWantToRead(book)
                        2 -> addToFinishedReading(book)
                    }
                }
                .show()
        }
    }
    }
    private fun displayBookDetails(book: Book) {
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val publisherTextView = findViewById<TextView>(R.id.publisherTextView)
        val articleImageView = findViewById<ImageView>(R.id.bookImageView)
        val isbnTextView = findViewById<TextView>(R.id.isbnTextView)
        val yearTextView = findViewById<TextView>(R.id.yearTextView)

        titleTextView.text = book.title ?: "Unknown Title"
        authorTextView.text = book.author ?: "Unknown Author"
        publisherTextView.text = book.publisher ?: "Unknown Publisher"
        isbnTextView.text = book.isbn ?: "Unknown ISBN"
        yearTextView.text = book.yearOfPublication ?: "Unknown Year"
        Glide.with(this)
            .load(book.cover)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_profile)
            .into(articleImageView)
    }


    private fun addToCurrentlyReading(book: Book) {
        val userId = getCurrentUserId()
        val currentlyReadingBook = CurrentlyReadingBook(0, userId, book.title, book.author,
            book.cover, 0)
        CoroutineScope(Dispatchers.IO).launch {
            val existingBook = book.title?.let { currentlyReadingBookDao.getBook(userId, it) }
            if (existingBook != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BookDetailActivity, "This book is already in your 'Currently Reading' list", Toast.LENGTH_SHORT).show()
                }
            } else {
                currentlyReadingBookDao.insertCurrentlyReadingBook(currentlyReadingBook)
            }
        }
    }
    private fun addToWantToRead(book: Book) {
        val userId = getCurrentUserId()
        val wantToReadBook = WantToReadBook(0, userId, book.title, book.author,
            book.cover)
        CoroutineScope(Dispatchers.IO).launch {
            val existingBook = book.title?.let { wantToReadBookDao.getBook(userId, it) }
            if (existingBook != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BookDetailActivity, "This book is already in your 'Want To Read' list", Toast.LENGTH_SHORT).show()
                }
            } else {
                wantToReadBookDao.insertWantToRead(wantToReadBook)
            }
        }
    }

    private fun addToFinishedReading(book: Book) {
        val userId = getCurrentUserId()
        val finishedReadingBook = FinishedReadingBook(0, userId, book.title, book.author,
            book.cover)
        CoroutineScope(Dispatchers.IO).launch {
            val existingBook = book.title?.let { finishedReadingBookDao.getBook(userId, it) }
            if (existingBook != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BookDetailActivity, "This book is already in your 'Finished Reading List", Toast.LENGTH_SHORT).show()
                }
            } else {
                finishedReadingBookDao.insertFinishedReading(finishedReadingBook)
            }
        }
    }
    private fun getCurrentUserId(): String {
        val userModel = runBlocking {
            userPreference.getSession().firstOrNull()
        }
        return userModel?.email ?: ""
    }
}



