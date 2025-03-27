package com.example.filmlibrary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.filmlibrary.R
import com.example.filmlibrary.navigation.Screen
import com.example.filmlibrary.data.Genre
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.data.loadProductions
import com.example.filmlibrary.ui.theme.DarkPink
import com.example.filmlibrary.ui.theme.DarkPurple
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1
import com.example.filmlibrary.ui.theme.TextH2
import com.example.filmlibrary.utils.filterProductionsByGenre
import com.example.filmlibrary.utils.filterProductionsByGenreAndWatchedStatus
import com.example.filmlibrary.utils.filterProductionsByTitle
import com.example.filmlibrary.utils.sortProductions


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var productions by remember {
        mutableStateOf(loadProductions(context) ?: emptyList())
    }
    val genres = Genre.entries
    val watchedStatusEntries = context.resources.getStringArray(R.array.isWatched).toList()
    val sortingEntries = context.resources.getStringArray(R.array.sorting).toList()

    var input by remember {
        mutableStateOf("")
    }

    var selectedGenreFilter = remember {
        mutableStateOf(0)
    }

    var selectedWatchedStatusFilter = remember {
        mutableStateOf(0)
    }

    var selectedSorting = remember {
        mutableStateOf(0)
    }

    val filteredProductionsByTitle = filterProductionsByTitle(
        productions,
        input,
    )

    val productionsFilteredByGenre: List<Production> = filterProductionsByGenre(
        productions,
        genres,
        selectedGenreFilter,
    )

    val productionsFilteredByGenreAndWatchedStatus = filterProductionsByGenreAndWatchedStatus(
        productionsFilteredByGenre,
        selectedWatchedStatusFilter,
    )

    val finalFilteredSortedProductions = sortProductions(
        productionsFilteredByGenreAndWatchedStatus,
        selectedSorting,
    )

    Column(
        modifier = Modifier
            .background(DarkPurple)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f
                    )
                )
        ) {
            TopBar()
            SearchBar(input, onInputChange = { input = it })
            FilterChips(
                genres,
                watchedStatusEntries,
                sortingEntries,
                selectedGenreFilter,
                selectedWatchedStatusFilter,
                selectedSorting
            )
            if (input.isNotBlank()) ProductionList(filteredProductionsByTitle, navController)
            else ProductionList(finalFilteredSortedProductions, navController)
        }
    }
}

@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 52.dp,
            ),
    ) {
        Text(
            text = stringResource(id = R.string.title1),
            color = TextH1,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)

        )
        Text(
            text = stringResource(id = R.string.title2),
            color = TextH2,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

    }
}

@Composable
fun <T> Chip(
    index: Int,
    list: List<T>,
    selectedFilter: MutableState<Int>,
    labelProvider: (T) -> String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = if (index == list.size - 1) 16.dp else 0.dp
            )
            .clickable {
                selectedFilter.value = index
            }
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selectedFilter.value == index) DarkPink
                else LightPurple
            )
            .height(48.dp)
            .padding(12.dp)
    ) {
        Text(
            text = labelProvider(list[index]),
            color = TextH1,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FilterChips(
    genres: List<Genre>,
    watchedStatusEntries: List<String>,
    sortingEntries: List<String>,
    selectedGenreFilter: MutableState<Int>,
    selectedWatchedStatusFilter: MutableState<Int>,
    selectedSorting: MutableState<Int>,
) {
    LazyRow {
        items(genres.size) { index ->
            Chip(
                index,
                genres,
                selectedGenreFilter,
                { it.name }
            )
        }
    }
    LazyRow(
        modifier = Modifier
            .padding(start = 48.dp)
    ) {
        items(watchedStatusEntries.size) { index ->
            Chip(
                index,
                watchedStatusEntries,
                selectedWatchedStatusFilter,
                { it }
            )
        }
    }
    LazyRow {
        items(sortingEntries.size) { index ->
            Chip(
                index,
                sortingEntries,
                selectedSorting,
                { it }
            )
        }
    }
}

@Composable
fun SearchBar(
    input: String,
    onInputChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
            .clip(RoundedCornerShape(22.dp))
            .background(TextH2),
    ) {
        if(input.isEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 4.dp
                        )
                        .size(32.dp),
                    tint = TextH1,
                )
                Text(
                    text = stringResource(id = R.string.search_bar),
                    color = TextH1,
                    fontSize = 22.sp,
                )
            }
        }
        BasicTextField(
            value = input,
            onValueChange = onInputChange,
            textStyle = TextStyle(
                color = TextH1,
                fontSize = 22.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .padding(
                    start = 16.dp,
                    top = 3.dp,
                )
                .height(30.dp),
        )
    }
}

@Composable
fun ProductionItem(
    production: Production? = null,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(22.dp))
            .fillMaxSize()
    ) {
        if (production != null) {
            production.imageUri?.let { imageUri ->
                AsyncImage(
                    model = imageUri,
                    contentDescription = production.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f
                        )
                    )
                    .clickable {
                        navController.navigate(Screen.ProductionDetailsScreen.route + "/${production.id}")
                    }
            )
            Text(
                text = production.title.uppercase(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextH1,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = 12.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 6.dp,
                        color = TextH2,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .clickable {
                        navController.navigate(Screen.AddProductionScreen.route)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "Add",
                    tint = TextH2,
                    modifier = Modifier
                        .size(128.dp)
                )
            }
        }
    }
}

@Composable
fun ProductionList(
    productions: List<Production>,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.list),
            color = TextH1,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                bottom = 50.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(productions.size) {
                ProductionItem(
                    productions[it],
                    navController,
                )
            }
            item {
                ProductionItem(
                    null,
                    navController,
                )
            }
        }
    }
}

