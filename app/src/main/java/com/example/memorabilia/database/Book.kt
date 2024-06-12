package com.example.memorabilia.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.memorabilia.api.response.Article
import com.example.memorabilia.api.response.Book
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
        val title: String?,
        val author: String?,
        val cover: String?
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
        val title: String?,
        val author: String?,
        val cover: String?
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


