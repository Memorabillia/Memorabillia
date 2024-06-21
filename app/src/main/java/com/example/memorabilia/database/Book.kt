package com.example.memorabilia.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "currently_reading")
data class CurrentlyReadingBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String ?,
    val author: String ?,
    val cover: String ?,
    val publisher : String ?,
    val isbn : String ?,
    val yearOfPublication : String ?,
    var progress: Int
) : Serializable


    @Entity(tableName = "want_to_read")
    data class WantToReadBook(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val userId: String,
        val title: String ?,
        val author: String ?,
        val cover: String ?,
        val publisher : String ?,
        val isbn : String ?,
        val yearOfPublication : String ?,
    ) : Serializable

    @Entity(tableName = "finished_reading")
    data class FinishedReadingBook(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val userId: String,
        val title: String ?,
        val author: String ?,
        val cover: String ?,
        val publisher : String ?,
        val isbn : String ?,
        val yearOfPublication : String ?,
        var notes: String ?
    ) : Serializable
