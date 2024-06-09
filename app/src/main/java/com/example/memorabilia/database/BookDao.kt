package com.example.memorabilia.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentlyReadingBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentlyReadingBook(book: CurrentlyReadingBook)

    @Query("SELECT * FROM currently_reading WHERE userId = :userId")
    suspend fun getAllBooks(userId:String): List<CurrentlyReadingBook>

    @Query("UPDATE currently_reading SET progress = :progress WHERE id = :id")
    suspend fun updateBookProgress(id: Int, progress: Int)
    @Query("SELECT * FROM currently_reading WHERE userId = :userId AND title = :title LIMIT 1")
    suspend fun getBook(userId: String, title: String): CurrentlyReadingBook?
    @Delete
    suspend fun deleteBook(book: CurrentlyReadingBook)
}
@Dao
interface WantToReadBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWantToRead(book: WantToReadBook)

    @Query("SELECT * FROM want_to_read WHERE userId = :userId")
    suspend fun getAllBooks(userId: String): List<WantToReadBook>

    @Query("SELECT * FROM want_to_read WHERE userId = :userId AND title = :title LIMIT 1")
    suspend fun getBook(userId: String, title: String): WantToReadBook?

    @Delete
    suspend fun deleteBook(book: WantToReadBook)
}
