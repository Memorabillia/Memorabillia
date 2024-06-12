package com.example.memorabilia.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class BookResponse(
    @SerializedName("books") val books: List<Book>

)


data class Book(
    @SerializedName("bookId") val bookid: String?,
    @SerializedName("Author") val author: String?,
    @SerializedName("Cover")val cover: String?,
    @SerializedName("ISBN")val isbn: String?,
    @SerializedName("Publisher")val publisher : String?,
    @SerializedName("Title")val title: String?,
    @SerializedName("Year-Of-Publication")val yearOfPublication: String?
) : Serializable