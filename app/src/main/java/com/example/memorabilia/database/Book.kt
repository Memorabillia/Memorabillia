package com.example.memorabilia.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.api.response.Book


@Entity(tableName = "currently_reading")
data class CurrentlyReadingBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String ?,
    val author: String ?,
    val cover: String ?,
    var progress: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    constructor(userId: String, book: Book, progress: Int) : this(
        0,
        userId,
        book.title,
        book.author,
        book.cover,
        progress
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(cover)
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
    val title: String ?,
    val author: String ?,
    val cover: String ?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(cover)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WantToReadBook> {
        override fun createFromParcel(parcel: Parcel): WantToReadBook {
            return WantToReadBook(parcel)
        }

        override fun newArray(size: Int): Array<WantToReadBook?> {
            return arrayOfNulls(size)
        }
    }
}

@Entity(tableName = "finished_reading")
data class FinishedReadingBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String ?,
    val author: String ?,
    val cover: String ?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(cover)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WantToReadBook> {
        override fun createFromParcel(parcel: Parcel): WantToReadBook {
            return WantToReadBook(parcel)
        }

        override fun newArray(size: Int): Array<WantToReadBook?> {
            return arrayOfNulls(size)
        }
    }
}
