package com.example.memorabilia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.api.response.NewsResponse
import com.example.memorabilia.currentlyreading.CurrentlyReadingActivity
import com.example.memorabilia.data.DummyData
import com.example.memorabilia.data.Repository
import com.example.memorabilia.databinding.ActivityMainBinding
import com.example.memorabilia.di.Injection
import com.example.memorabilia.search.SearchActivity
import com.example.memorabilia.settings.SettingsActivity
import com.example.memorabilia.theme.ThemeViewModel
import com.example.memorabilia.wanttoread.WantToReadActivity
import com.example.memorabilia.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private lateinit var apiService: ApiService
    private lateinit var adapter: MainAdapter

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = Injection.provideRepository(applicationContext)

        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is logged in
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        // Setup RecyclerView
        val recyclerView = binding.recommendationsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter()
        recyclerView.adapter = adapter

        // Initialize ApiService
        apiService = ApiConfig.getNewsApi()

        // Setup BottomNavigationView
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.homenav
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homenav -> true
                R.id.searchnav -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
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


        val buttonCurrentlyReading = binding.buttonCurrentlyReading
        buttonCurrentlyReading.setOnClickListener {
            val intent = Intent(this, CurrentlyReadingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val buttonWantToRead = binding.buttonWantToRead
        buttonWantToRead.setOnClickListener {
            val intent = Intent(this, WantToReadActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

//        val buttonFinished = binding.buttonFinishedReading
//        buttonFinished.setOnClickListener {
//            val intent = Intent(this, FinishedReadingActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//        }


        // Show books category
        showBooksArticles()
    }

    private fun showBooksArticles() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response: Response<NewsResponse> = apiService.searchArticles("books", "db03c64333b7461da81b46755c01d5dc")
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        adapter.setData(it.articles)
                    } ?: run {
                        // Use dummy data if response body is null
                        adapter.setData(DummyData.getDummyArticles())
                    }
                } else {
                    // Use dummy data if API call is not successful
                    adapter.setData(DummyData.getDummyArticles())
                    Toast.makeText(applicationContext, "Failed to load articles from API", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Use dummy data if an exception occurs
                adapter.setData(DummyData.getDummyArticles())
                Toast.makeText(applicationContext, "Failed to load articles. Using dummy data.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
