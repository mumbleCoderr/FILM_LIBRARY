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
import java.util.UUID

enum class Genre {
    ALL, DRAMA, COMEDY, ACTION, THRILLER, HORROR, DOCUMENTARY, ROMANCE, FANTASY,
}
enum class ProductionType(var durationOrParts: Int){
    MOVIE(0), SERIES(0);

    fun withDurationOrParts(value: Int): ProductionType {
        return when (this) {
            MOVIE -> MOVIE.apply { durationOrParts = value }
            SERIES -> SERIES.apply { durationOrParts = value }
        }
    }
}


data class Production(
    val id: UUID = UUID.randomUUID(),
    var title: String = "Add title",
    var genre: Genre = Genre.ALL,
    var releaseDate: LocalDate = LocalDate.now(),
    var isWatched: Boolean = false,
    var comment: String = "Leave a comment",
    var rate: Int = 0,
    var imageUri: String? = null,
    var productionType: ProductionType = ProductionType.MOVIE,
): Serializable{
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


