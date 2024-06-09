package com.example.memorabilia.data

import com.example.memorabilia.api.response.Article
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.api.response.Source

object DummyData {
    fun getDummyBooks(): List<Book> {
        return listOf(
            Book(
                title = "Dummy Article 1",
                author = "Author 1",
                cover = "http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg",
                isbn = "0195153448",
                publisher = "Oxford University Press",
                yearOfPublication = "2002"


            ),
            Book(
                title = "Dummy Article 2",
                author = "Author 2",
                cover = "http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg",
                isbn = "0195153448",
                publisher = "Oxford University Press",
                yearOfPublication = "2002"

            ),
            Book(
                title = "Dummy Article 3",
                author = "Author 3",
                cover = "http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg",
                isbn = "0195153448",
                publisher = "Oxford University Press",
                yearOfPublication = "2002"

            ),
            Book(
                title = "Dummy Article 4",
                author = "Author 4",
                cover = "http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg",
                isbn = "0195153448",
                publisher = "Oxford University Press",
                yearOfPublication = "2002"

            ),
            Book(
                title = "Dummy Article 5",
                author = "Author 5",
                cover = "http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg",
                isbn = "0195153448",
                publisher = "Oxford University Press",
                yearOfPublication = "2002"

            )
        )
    }

        fun getDummyArticles(): List<Article> {
            return listOf(
                Article(
                    title = "Dummy Article 1",
                    author = "Author 1",
                    description = "Description of Dummy Article 1",
                    url = "https://example.com/article1",
                    urlToImage = "https://static-cse.canva.com/blob/1427892/ColorfulIllustrationYoungAdultBookCover.jpg",
                    publishedAt = "2024-06-05",
                    content = "Content of Dummy Article 1",
                    source = Source(id = null, name = "Source of Dummy Article 1")

                ),
                Article(
                    title = "Dummy Article 2",
                    author = "Author 2",
                    description = "Description of Dummy Article 2",
                    url = "https://example.com/article2",
                    urlToImage = "https://static-cse.canva.com/blob/1427892/ColorfulIllustrationYoungAdultBookCover.jpg",
                    publishedAt = "2024-06-05",
                    content = "Content of Dummy Article 2",
                    source = Source(id = null, name = "Source of Dummy Article 2")
                ),
                Article(
                    title = "Dummy Article 3",
                    author = "Author 3",
                    description = "Description of Dummy Article 2",
                    url = "https://example.com/article2",
                    urlToImage = "https://static-cse.canva.com/blob/1427892/ColorfulIllustrationYoungAdultBookCover.jpg",
                    publishedAt = "2024-06-05",
                    content = "Content of Dummy Article 3",
                    source = Source(id = null, name = "Source of Dummy Article 3")
                ),
                Article(
                    title = "Dummy Article 4",
                    author = "Author 4",
                    description = "Description of Dummy Article 4",
                    url = "https://example.com/article2",
                    urlToImage = "https://static-cse.canva.com/blob/1427892/ColorfulIllustrationYoungAdultBookCover.jpg",
                    publishedAt = "2024-06-05",
                    content = "Content of Dummy Article 4",
                    source = Source(id = null, name = "Source of Dummy Article 4")
                ),
                Article(
                    title = "Dummy Article 5",
                    author = "Author 5",
                    description = "Description of Dummy Article 5",
                    url = "https://example.com/article2",
                    urlToImage = "https://static-cse.canva.com/blob/1427892/ColorfulIllustrationYoungAdultBookCover.jpg",
                    publishedAt = "2024-06-05",
                    content = "Content of Dummy Article 5",
                    source = Source(id = null, name = "Source of Dummy Article 5")
                )
            )
        }
}

