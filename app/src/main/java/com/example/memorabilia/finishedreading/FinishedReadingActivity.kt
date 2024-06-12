package com.example.memorabilia.finishedreading

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.database.FinishedReadingBookDao
import com.example.memorabilia.database.WantToReadBookDao
import com.example.memorabilia.search.SearchAdapter
import com.example.memorabilia.wanttoread.WantToReadAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
private lateinit var userPreference: UserPreference
class FinishedReadingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FinishedListAdapter
    private lateinit var finishedReadingBookDao: FinishedReadingBookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_reading)

        userPreference = UserPreference.getInstance(this.dataStore)
        finishedReadingBookDao = BookDatabase.getDatabase(this).finishedReadingBookDao()

        recyclerView = findViewById(R.id.finishedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FinishedListAdapter(finishedReadingBookDao)
        recyclerView.adapter = adapter
        displayFinishedReadingBooks()
    }

    private fun displayFinishedReadingBooks() {
        val userId = getCurrentUserId()
        CoroutineScope(Dispatchers.IO).launch {
            val books = finishedReadingBookDao.getAllBooks(userId)
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

