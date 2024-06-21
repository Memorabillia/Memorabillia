package com.example.memorabilia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrentlyReadingBook::class, WantToReadBook::class, FinishedReadingBook::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun currentlyReadingBookDao(): CurrentlyReadingBookDao
    abstract fun wantToReadBookDao(): WantToReadBookDao

    abstract fun finishedReadingBookDao(): FinishedReadingBookDao



    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
