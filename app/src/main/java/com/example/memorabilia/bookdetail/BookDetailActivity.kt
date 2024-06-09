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
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.api.response.Book
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

        val isbn = intent.getStringExtra("isbn")
        if (isbn != null) {
            fetchBookDetails(isbn)
        }
    }

    private fun fetchBookDetails(isbn: String) {
        val book = fetchBookDetailsFromAPI(isbn)
        displayBookDetails(book)
    }

    private fun fetchBookDetailsFromAPI(isbn: String): Book {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val token = runBlocking {
            userPreference.getSession().firstOrNull()?.token ?: ""
        }

        // Use ApiConfig to get the ApiService
        val apiService = ApiConfig.getApiService(token)

        // Make a network request to fetch book details using the provided ISBN
        val response = apiService.getBookDetails("isbn:$isbn")

        // Check if the request was successful and the response body is not null
        if (response.isSuccessful && response.body() != null) {
            // Return the first book from the response (assuming it's a list of books)
            return response.body()!!.books.first()
        } else {
            // Handle the case when the request fails or the response body is null
            throw Exception("Failed to fetch book details")
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


//    private fun addToCurrentlyReading(book: Book) {
//        val userId = getCurrentUserId()
//        val currentlyReadingBook = CurrentlyReadingBook(0, userId, book.title, book.author,
//            book.cover, 0)
//        CoroutineScope(Dispatchers.IO).launch {
//            currentlyReadingBookDao.insertCurrentlyReadingBook(currentlyReadingBook)
//        }
//    }

    private fun getCurrentUserId(): String {
        val userModel = runBlocking {
            userPreference.getSession().firstOrNull()
        }
        return userModel?.email ?: ""
    }
}



