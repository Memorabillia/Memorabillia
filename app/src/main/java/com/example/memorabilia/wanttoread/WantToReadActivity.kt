package com.example.memorabilia.wanttoread

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorabilia.R
import com.example.memorabilia.database.BookDatabase
import com.example.memorabilia.search.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class WantToReadActivity : AppCompatActivity() {
//    @SuppressLint("MissingInflatedId")
//
//    private val wantToReadBooks: MutableList<SearchAdapter.Book> = mutableListOf()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_want_to_read)
//
//        val recyclerView = findViewById<RecyclerView>(R.id.WantToReadRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        val adapter = WantToReadAdapter(wantToReadBooks)
//        recyclerView.adapter = adapter
//
//        val bookDatabase = BookDatabase.getDatabase(this)
//        val bookDao = bookDatabase.wantToReadBookDao()
//        CoroutineScope(Dispatchers.IO).launch {
//            val books = bookDao.getAll()
//            withContext(Dispatchers.Main) {
//                wantToReadBooks.addAll(books.map {
//                    SearchAdapter.Book(it.id,it.title, it.author, it.imageUrl, it.synopsis, it.rating)
//                })
//                adapter.notifyDataSetChanged()
//            }
//        }
//
//        // Retrieve the book's information passed from BookDetailActivity
//        val bookTitle = intent.getStringExtra("BOOK_TITLE")
//        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
//        val bookImageResId = intent.getIntExtra("BOOK_IMAGE", 0)
//
//
//        if (bookTitle != null && bookAuthor != null && bookImageResId != 0) {
//            val newBook = SearchAdapter.Book(0,bookTitle, bookAuthor, bookImageResId, "", 0.0f)
//            wantToReadBooks.add(newBook)
//            adapter.notifyItemInserted(wantToReadBooks.size - 1)
//            val book = WantToReadBook(0, bookTitle, bookAuthor, bookImageResId, "", 0.0f)
//            CoroutineScope(Dispatchers.IO).launch {
//                bookDao.insert(book)
//            }
//        }
//    }
//}
