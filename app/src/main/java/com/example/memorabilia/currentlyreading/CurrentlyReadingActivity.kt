package com.example.memorabilia.currentlyreading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.bookdetail.BookDetailActivity
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.database.CurrentlyReadingBook
import com.example.memorabilia.database.CurrentlyReadingBookDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
private lateinit var userPreference: UserPreference

class CurrentlyReadingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrentlyReadingAdapter
    private lateinit var currentlyReadingBookDao: CurrentlyReadingBookDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currently_reading)

        userPreference = UserPreference.getInstance(this.dataStore)
        currentlyReadingBookDao = BookDatabase.getDatabase(this).currentlyReadingBookDao()

        recyclerView = findViewById(R.id.rvCurrentlyReading)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrentlyReadingAdapter(currentlyReadingBookDao)
        recyclerView.adapter = adapter



        displayCurrentlyReadingBooks()
    }

    private fun displayCurrentlyReadingBooks() {
        val userId = getCurrentUserId()
        CoroutineScope(Dispatchers.IO).launch {
            val books = currentlyReadingBookDao.getAllBooks(userId)
            withContext(Dispatchers.Main) {
                adapter.setData(books)
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
