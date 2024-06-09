package com.example.memorabilia.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.api.response.BookResponse
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
import com.example.memorabilia.main.MainActivity
import com.example.memorabilia.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val userPreference = UserPreference.getInstance(applicationContext.dataStore)

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.token.isNotEmpty()) {
                apiService = ApiConfig.getApiService(user.token)
                setupUI()
            } else {
                Toast.makeText(this@SearchActivity, "No token found, please login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupUI() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.searchnav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homenav -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.searchnav -> true
                R.id.profilenav -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        recyclerView = findViewById<RecyclerView>(R.id.rvSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this)
        recyclerView.adapter = adapter

        val searchEditText = findViewById<EditText>(R.id.edSearchBook)
        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            searchBooks(query)
        }
    }

    private fun searchBooks(query: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response: Response<List<Book>> = apiService.searchBooks(query)
                if (response.isSuccessful) {
                    val books = response.body()
                    if (books != null) {
                        if (books.isNotEmpty()) {
                            adapter.setData(books)

                            for (book in books) {
                                Log.d("Book", "Title: ${book.title}, Author: ${book.author}, Cover: ${book.cover}")
                            }
                        } else {
                            Toast.makeText(applicationContext, "No books found for \"$query\"", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Response body is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to load books from API", Toast.LENGTH_SHORT).show()
                    Log.d("SearchActivity", "Response: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Failed to load books from API", Toast.LENGTH_SHORT).show()
                Log.e("SearchActivity", "Error: ${e.message}", e)
            }
            progressBar.visibility = View.GONE
        }
    }



}
