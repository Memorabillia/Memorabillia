package com.example.memorabilia.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.api.response.NewsResponse
import com.example.memorabilia.data.DummyData
import com.example.memorabilia.main.MainActivity
import com.example.memorabilia.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Random

class SearchActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        apiService = ApiConfig.getNewsApi()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.searchnav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homenav -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.searchnav -> {
                    true
                }
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
        adapter = SearchAdapter()
        recyclerView.adapter = adapter

        val searchEditText = findViewById<EditText>(R.id.edSearchBook)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchArticles(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        // Fetch random articles on app start
        val randomQueries = listOf("world", "business", "technology", "entertainment", "sports", "health")
        val randomQuery = randomQueries[Random().nextInt(randomQueries.size)]
        searchArticles(randomQuery)
    }

    private fun searchArticles(query: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response: Response<NewsResponse> = apiService.searchArticles(query, "db03c64333b7461da81b46755c01d5dc")
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        adapter.setData(it.articles)
                    }
                    progressBar.visibility = View.GONE // Menyembunyikan ProgressBar setelah data berhasil dimuat
                } else {
                    Toast.makeText(applicationContext, "Failed to load articles from API, using dummy data", Toast.LENGTH_SHORT).show()
                    loadDummyData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Failed to load articles from API, using dummy data", Toast.LENGTH_SHORT).show()
                loadDummyData()
            }
        }
    }

    private fun loadDummyData() {
        val dummyArticles = DummyData.getDummyArticles()
        adapter.setData(dummyArticles)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
    }
}
