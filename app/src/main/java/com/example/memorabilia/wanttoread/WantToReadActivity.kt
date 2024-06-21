package com.example.memorabilia.wanttoread

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.database.CurrentlyReadingBookDao
import com.example.memorabilia.database.WantToReadBookDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
private lateinit var userPreference: UserPreference
class WantToReadActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WantToReadAdapter
    private lateinit var wantToReadBookDao: WantToReadBookDao
    private lateinit var currentlyReadingBookDao: CurrentlyReadingBookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_want_to_read)

        userPreference = UserPreference.getInstance(this.dataStore)
        wantToReadBookDao = BookDatabase.getDatabase(this).wantToReadBookDao()
        currentlyReadingBookDao = BookDatabase.getDatabase(this).currentlyReadingBookDao()

        recyclerView = findViewById(R.id.WantToReadRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WantToReadAdapter(wantToReadBookDao,currentlyReadingBookDao)
        recyclerView.adapter = adapter
        displayWantToReadBooks()
    }

    private fun displayWantToReadBooks() {
        val userId = getCurrentUserId()
        CoroutineScope(Dispatchers.IO).launch {
            val books = wantToReadBookDao.getAllBooks(userId)
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
