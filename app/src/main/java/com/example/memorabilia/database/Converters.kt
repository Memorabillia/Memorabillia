package com.example.memorabilia.database

import androidx.room.TypeConverter
import com.example.memorabilia.api.response.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromArticle(article: Article): String {
        return Gson().toJson(article)
    }

    @TypeConverter
    fun toArticle(articleString: String): Article {
        val articleType = object : TypeToken<Article>() {}.type
        return Gson().fromJson(articleString, articleType)
    }
}
