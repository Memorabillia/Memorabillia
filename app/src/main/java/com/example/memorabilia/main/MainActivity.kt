package com.example.memorabilia.main



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.currentlyreading.CurrentlyReadingActivity
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
import com.example.memorabilia.databinding.ActivityMainBinding
import com.example.memorabilia.di.Injection
import com.example.memorabilia.finishedreading.FinishedReadingActivity
import com.example.memorabilia.search.SearchActivity
import com.example.memorabilia.settings.SettingsActivity
import com.example.memorabilia.wanttoread.WantToReadActivity
import com.example.memorabilia.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var adapter: MainAdapter
    private var tokenInvalidMessageShown = false

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is logged in
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.fetchBookRecommendations() // Fetch book recommendations when user is logged in
            }
        }

        val userPreference = UserPreference.getInstance(applicationContext.dataStore)

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.token.isNotEmpty()) {
                apiService = ApiConfig.getApiService(user.token)
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

        val recyclerView = binding.recommendationsRecyclerView
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        adapter = MainAdapter()
        recyclerView.adapter = adapter


        binding.progressBar.visibility = View.VISIBLE

        viewModel.bookRecommendations.observe(this) { books ->
            binding.progressBar.visibility = View.GONE
            if (books != null) {
                adapter.setData(books)
            } else {
                Toast.makeText(this, "Failed to load recommendations", Toast.LENGTH_SHORT).show()
            }
        }


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





        val buttonFinished = binding.buttonFinishedReading
        buttonFinished.setOnClickListener {
            val intent = Intent(this, FinishedReadingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }



    }

}
