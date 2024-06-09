package com.example.memorabilia.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.api.response.NewsResponse
import com.example.memorabilia.currentlyreading.CurrentlyReadingActivity
import com.example.memorabilia.data.DummyData
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private lateinit var apiService: ApiService
    private lateinit var adapter: MainAdapter
    private var tokenInvalidMessageShown = false


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

        val userPreference = UserPreference.getInstance(applicationContext.dataStore)

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.token.isNotEmpty()) {
                apiService = ApiConfig.getApiService(user.token)
                showRandomBooks()
            } else {
                if (!tokenInvalidMessageShown) {
                    tokenInvalidMessageShown = true
                    Toast.makeText(
                        this@MainActivity,
                        "Invalid token or token not found, please login",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Invalid token or token not found")
                }
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                finish()
            }
        }

        // Setup RecyclerView
        val recyclerView = binding.recommendationsRecyclerView
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
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
//
//        val buttonFinished = binding.buttonFinishedReading
//        buttonFinished.setOnClickListener {
//            val intent = Intent(this, FinishedReadingActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//        }



    }

    private fun showRandomBooks() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.Main) {
            var retryCount = 0
            var success = false
            while (!success && retryCount < 5) {
                try {
                    val response: Response<List<Book>> = apiService.getAllBooks()
                    if (response.isSuccessful) {
                        val books = response.body()?.shuffled()?.take(20) // Ambil maksimal 20 buku secara acak
                        if (books != null) {
                            adapter.setData(books)
                            success = true
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Response body is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to load books from API",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("MainActivity", "Response: ${response.code()} - ${response.message()}")
                    }
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    retryCount++
                    Log.e("MainActivity", "Error: ${e.message}, retrying...")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        applicationContext,
                        "Failed to load books from API",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Error: ${e.message}", e)
                }
            }
            progressBar.visibility = View.GONE
        }
    }



}
