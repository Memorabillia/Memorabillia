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
}