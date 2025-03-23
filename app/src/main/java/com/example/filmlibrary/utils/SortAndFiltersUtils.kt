package com.example.filmlibrary.utils

import androidx.compose.runtime.MutableState
import com.example.filmlibrary.data.Genre
import com.example.filmlibrary.data.Production

fun filterProductionsByTitle(
    productions: List<Production>,
    input: String,
): List<Production>{
    return productions.filter {
        it.title.contains(input, ignoreCase = true)
    }
}

fun filterProductionsByGenre(
    productions: List<Production>,
    genres: List<Genre>,
    selectedGenreFilter: MutableState<Int>,
): List<Production> {
    if (selectedGenreFilter.value == 0) {
        return productions
    } else {
        return productions.filter {
            it.genre.name == genres[selectedGenreFilter.value].name
        }
    }
}

fun filterProductionsByGenreAndWatchedStatus(
    productionsFilteredByGenre: List<Production>,
    selectedWatchedStatusFilter: MutableState<Int>,
): List<Production> {
    return productionsFilteredByGenre.filter {
        when (selectedWatchedStatusFilter.value) {
            1 -> it.isWatched
            2 -> !it.isWatched
            else -> true
        }
    }
}

fun sortProductions(
    productions: List<Production>,
    selectedSorting: MutableState<Int>,
): List<Production> {
    return when (selectedSorting.value) {
        0 -> productions.sortedByDescending { it.releaseDate }
        1 -> productions.sortedBy { it.releaseDate }
        2 -> productions.sortedBy { it.title }
        3 -> productions.sortedByDescending { it.title }
        else -> productions
    }
}