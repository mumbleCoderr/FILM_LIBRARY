package com.example.filmlibrary.data


import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.filmlibrary.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.time.LocalDate

enum class Genre {
    ALL, DRAMA, COMEDY, ACTION, THRILLER, HORROR, DOCUMENTARY, ROMANCE, FANTASY
}

open class Production(
    var title: String,
    var genre: Genre,
    var releaseDate: LocalDate,
    var isWatched: Boolean = false,
    var comment: String? = null,
    var rate: Int? = null,
    var imageUri: String? = null,
): Serializable{
    init {
        if (rate != null && (rate !in 1..5)) {
            throw IllegalArgumentException("rate has to be in 1..5")
        }

        if (comment != null && comment!!.isBlank()) {
            throw IllegalArgumentException("can't set empty comment")
        }
    }

    open fun deepCopy(): Production {
        return Production(
            title = this.title,
            genre = this.genre,
            releaseDate = this.releaseDate,
            isWatched = this.isWatched,
            comment = this.comment,
            rate = this.rate,
            imageUri = this.imageUri
        )
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
    imageUri: String? = null,
    var durationInMinutes: Int
) : Production(title, genre, releaseDate, isWatched, comment, rate, imageUri){

    override fun deepCopy(): Production {
        return Movie(
            title = this.title,
            genre = this.genre,
            releaseDate = this.releaseDate,
            isWatched = this.isWatched,
            comment = this.comment,
            rate = this.rate,
            imageUri = this.imageUri,
            durationInMinutes = this.durationInMinutes
        )
    }

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
    imageUri: String? = null,
    var parts: MutableMap<Int, Int>
): Production(title, genre, releaseDate, isWatched, comment, rate, imageUri){

    override fun deepCopy(): Production {
        return Series(
            title = this.title,
            genre = this.genre,
            releaseDate = this.releaseDate,
            isWatched = this.isWatched,
            comment = this.comment,
            rate = this.rate,
            imageUri = this.imageUri,
            parts = this.parts.toMutableMap()
        )
    }

    override fun toString(): String {
        return super.toString() +
                "parts: ${parts.entries.joinToString {"${it.key}:${it.value}"}}"
    }
}

fun saveProductions(context: Context, productions: List<Production>){
    val file = File(context.filesDir, "productions.ser")
    ObjectOutputStream(FileOutputStream(file)).use {
        it.writeObject(productions)
    }
}

fun loadProductions(context: Context): List<Production>? {
    val file = File(context.filesDir, "productions.ser")
    if (!file.exists()) return null
    ObjectInputStream(FileInputStream(file)).use {
        return it.readObject() as? List<Production>
    }
}


