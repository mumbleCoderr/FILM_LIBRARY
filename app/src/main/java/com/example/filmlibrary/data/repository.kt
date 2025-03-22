package com.example.filmlibrary.data


import android.media.Image
import android.net.Uri
import java.time.LocalDate
import java.util.Date

enum class Genre {
    DRAMA, COMEDY, ACTION, THRILLER, HORROR, DOCUMENTARY, ROMANCE, FANTASY
}

open class Production(
    var title: String,
    var genre: Genre,
    var releaseDate: LocalDate,
    var isWatched: Boolean = false,
    var comment: String? = null,
    var rate: Int? = null,
    var imageUri: Uri? = null,
){
    init {
        if (rate != null && (rate !in 1..10)) {
            throw IllegalArgumentException("rate has to be in 1..10")
        }

        if (comment != null && comment!!.isBlank()) {
            throw IllegalArgumentException("can't set empty comment")
        }
    }

    override fun toString(): String {
        return """
                        Production: ${this.javaClass.simpleName}
                        title: $title
                        genre: $genre
                        release date: $releaseDate
                        is watched?: $isWatched
                        comment: $comment
                        rate: $rate
        """.trimIndent()
    }
}

class Movie(
    title: String,
    genre: Genre,
    releaseDate: LocalDate,
    isWatched: Boolean = false,
    comment: String? = null,
    rate: Int? = null,
    imageUri: Uri? = null,
    val durationInMinutes: Int
) : Production(title, genre, releaseDate, isWatched, comment, rate, imageUri){
    override fun toString(): String {
        return super.toString() +
                "duration: ${durationInMinutes/60} ${durationInMinutes%60}"
    }
}

class Series(
    title: String,
    genre: Genre,
    releaseDate: LocalDate,
    isWatched: Boolean = false,
    comment: String? = null,
    rate: Int? = null,
    imageUri: Uri? = null,
    val parts: Map<Int, Int>
): Production(title, genre, releaseDate, isWatched, comment, rate, imageUri){
    override fun toString(): String {
        return super.toString() +
                "parts: ${parts.entries.joinToString {"${it.key}:${it.value}"}}"
    }
}

fun getProductions() =
    listOf<Production>(
        Movie(
            title = "Inception",
            genre = Genre.ACTION,
            releaseDate = LocalDate.of(2010, 7, 30),
            isWatched = true,
            comment = "I was shocked during that movie",
            rate = 10,
            durationInMinutes = 148
        ),
        Movie(
            title = "TeneT",
            genre = Genre.ACTION,
            releaseDate = LocalDate.of(2020, 8, 26),
            isWatched = true,
            comment = "I understood nothing",
            rate = 7,
            durationInMinutes = 150
        ),
        Movie(
            title = "Nosferatu",
            genre = Genre.HORROR,
            releaseDate = LocalDate.of(2024, 2, 21),
            isWatched = true,
            rate = 8,
            durationInMinutes = 132
        ),
        Movie(
            title = "Challengers",
            genre = Genre.ROMANCE,
            releaseDate = LocalDate.of(2024, 4, 26),
            isWatched = true,
            comment = "Ehh that ending...",
            rate = 7,
            durationInMinutes = 131
        ),
        Movie(
            title = "The Substance",
            genre = Genre.HORROR,
            releaseDate = LocalDate.of(2024, 9, 20),
            isWatched = true,
            comment = "Disgusting CGI - very good body horror",
            rate = 9,
            durationInMinutes = 140
        ),
        Movie(
            title = "Heretics",
            genre = Genre.HORROR,
            releaseDate = LocalDate.of(2024, 11, 8),
            isWatched = true,
            comment = "lack of action but very interesting plot. Unfortunately fatal ending",
            rate = 7,
            durationInMinutes = 111
        ),
        Series(
            title = "The Vikings",
            genre = Genre.ACTION,
            releaseDate = LocalDate.of(2013, 3, 3),
            isWatched = true,
            comment = "For me, that was the best thing what Ive seen in my whole life",
            rate = 10,
            parts = mapOf(1 to 9, 2 to 10, 3 to 10, 4 to 20, 5 to 20, 6 to 20)
        ),
        Series(
            title = "Breaking Bad",
            genre = Genre.DRAMA,
            releaseDate = LocalDate.of(2008, 1, 20),
            isWatched = true,
            comment = "Main character's behaviour change was the best which Ive seen",
            rate = 10,
            parts = mapOf(1 to 7, 2 to 13, 3 to 13, 4 to 13, 5 to 16)
        ),
        Series(
            title = "Peaky Blinders",
            genre = Genre.ACTION,
            releaseDate = LocalDate.of(2013, 9, 12),
            isWatched = true,
            comment = "really masterpiece",
            rate = 10,
            parts = mapOf(1 to 6, 2 to 6, 3 to 6, 4 to 6, 5 to 6, 6 to 6)
        ),
        Series(
            title = "The Witcher",
            genre = Genre.FANTASY,
            releaseDate = LocalDate.of(2019, 12, 20),
            isWatched = true,
            comment = "one man makes this series watchable - Henry Cavill",
            rate = 6,
            parts = mapOf(1 to 8, 2 to 8, 3 to 8)
        ),
        Series(
            title = "Black Mirror",
            genre = Genre.THRILLER,
            releaseDate = LocalDate.of(2011, 12, 4),
            isWatched = true,
            comment = "every piece makes you fill bad and influence you to think about human behaviour",
            rate = 9,
            parts = mapOf(1 to 3, 2 to 4, 3 to 6, 4 to 6, 5 to 3, 6 to 5, 7 to 6)
        ),
        Series(
            title = "Love, Death & Robots",
            genre = Genre.THRILLER,
            releaseDate = LocalDate.of(2019, 3, 15),
            isWatched = true,
            comment = "very similar to black mirror, but really beautiful animations",
            rate = 9,
            parts = mapOf(1 to 18, 2 to 8, 3 to 9)
        ),
    )


