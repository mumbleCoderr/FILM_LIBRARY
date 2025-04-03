package com.example.filmlibrary.ui.screen

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.filmlibrary.R
import com.example.filmlibrary.navigation.Screen
import com.example.filmlibrary.data.Genre
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.data.ProductionType
import com.example.filmlibrary.data.loadProductions
import com.example.filmlibrary.data.saveProductions
import com.example.filmlibrary.ui.theme.DarkGray
import com.example.filmlibrary.ui.theme.DarkPink
import com.example.filmlibrary.ui.theme.DarkPurple
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1
import com.example.filmlibrary.ui.theme.TextH2
import com.example.filmlibrary.utils.byteArrayToBitmap
import com.example.filmlibrary.utils.filterProductionsByGenre
import com.example.filmlibrary.utils.filterProductionsByGenreAndWatchedStatus
import com.example.filmlibrary.utils.filterProductionsByTitle
import com.example.filmlibrary.utils.sortProductions
import com.example.filmlibrary.utils.uriToByteArray
import java.time.LocalDate


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var productions by remember {
        mutableStateOf(mutableListOf<Production>().apply {
            addAll(loadProductions(context) ?: emptyList())
        })
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
    val productionsFilteredByGenre = filterProductionsByGenre(
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
            SearchBar(
                input,
                onInputChange = { input = it },
                keyboardController = keyboardController!!,
            )
            FilterChips(
                genres,
                watchedStatusEntries,
                sortingEntries,
                selectedGenreFilter,
                selectedWatchedStatusFilter,
                selectedSorting
            )
            if (input.isNotBlank()) {
                ProductionList(
                    productions = filteredProductionsByTitle,
                    navController = navController,
                    onDelete = { selectedProduction ->
                        productions = productions.toMutableList().apply {
                            remove(selectedProduction)
                        }
                        saveProductions(context, productions)
                    }
                )
            } else {
                ProductionList(
                    productions = finalFilteredSortedProductions,
                    navController = navController,
                    onDelete = { selectedProduction ->
                        productions = productions.toMutableList().apply {
                            remove(selectedProduction)
                        }
                        saveProductions(context, productions)
                    }
                )
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 52.dp,
                end = 16.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
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
    keyboardController: SoftwareKeyboardController
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
        if (input.isEmpty()) {
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController.hide()
                }
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductionItem(
    production: Production? = null,
    navController: NavController,
    onLongPress: (Production) -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(22.dp))
            .fillMaxSize()
    ) {
        if (production != null) {
            if (production.imageByteArray.isNotEmpty()) {
                Image(
                    bitmap = byteArrayToBitmap(production.imageByteArray).asImageBitmap(),
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
                    .combinedClickable(
                        onClick = { navController.navigate(Screen.ProductionDetailsScreen.route + "?productionId=${production.id}") },
                        onLongClick = { onLongPress(production) }
                    )
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
                        navController.navigate(Screen.ProductionDetailsScreen.route)
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
    onDelete: (Production?) -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var selectedProduction by remember {
        mutableStateOf<Production?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.list) + if (productions.size == 1)
                " ${productions.size} production"
            else " ${productions.size} productions",
            color = TextH1,
            fontSize = 30.sp,
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
                    production = productions[it],
                    navController = navController,
                    onLongPress = { production ->
                        selectedProduction = production
                        showDialog = true
                    }
                )
            }
            item {
                ProductionItem(
                    production = null,
                    navController = navController,
                    onLongPress = {}
                )
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    colors = ButtonColors(
                        containerColor = DarkPurple,
                        contentColor = TextH1,
                        disabledContainerColor = LightPurple,
                        disabledContentColor = TextH2,
                    ),
                    onClick = {
                        onDelete(selectedProduction)
                        showDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.delete_dialog_button_1))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonColors(
                        containerColor = DarkPurple,
                        contentColor = TextH1,
                        disabledContainerColor = LightPurple,
                        disabledContentColor = TextH2,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.delete_dialog_button_2))
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.delete_dialog_1),
                    color = TextH1,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.delete_dialog_2) + "\n${selectedProduction?.title}?",
                    color = TextH2,
                    fontSize = 16.sp
                )
            },
            containerColor = DarkGray,
        )
    }
}

