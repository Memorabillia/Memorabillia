package com.example.memorabilia.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.memorabilia.api.response.Article
/*
@Entity(tableName = "books")

data class Book(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val publicationYear: Int,
    val genre: List<String>,
    val description: String,
    val coverImage: String
)*/

@Entity(tableName = "currently_reading")
data class CurrentlyReadingBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String,
    val author: String,
    val urlToImage: String,
    var progress: Int,
    val description: String = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(urlToImage)
        parcel.writeInt(progress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrentlyReadingBook> {
        override fun createFromParcel(parcel: Parcel): CurrentlyReadingBook {
            return CurrentlyReadingBook(parcel)
        }

        override fun newArray(size: Int): Array<CurrentlyReadingBook?> {
            return arrayOfNulls(size)
        }
    }
}
@Entity(tableName = "want_to_read")
data class WantToReadBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String,
    val author: String,
    val urlToImage: String
)

@Entity(tableName = "finished_reading")
data class FinishedReadingArticle(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "article") val article: Article
)