package com.example.filmlibrary.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.filmlibrary.R
import com.example.filmlibrary.data.Genre
import com.example.filmlibrary.data.Movie
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.data.Series
import com.example.filmlibrary.data.loadProductions
import com.example.filmlibrary.data.saveProductions
import com.example.filmlibrary.ui.theme.DarkGray
import com.example.filmlibrary.ui.theme.DarkPurple
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1
import com.example.filmlibrary.ui.theme.TextH2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ProductionDetailsScreen(productionTitle: String?) {
    val context = LocalContext.current
    var productions by remember {
        mutableStateOf(loadProductions(context) ?: emptyList())
    }
    val production = productions.find {
        it.title == productionTitle
    } ?: Production(
        title = productionTitle ?: "add title",
        genre = Genre.ALL,
        releaseDate = LocalDate.now(),
        comment = "leave a comment"
    )
    val genres = Genre.entries
    var comment by remember {
        mutableStateOf(production.comment)
    }
    var rating by remember {
        mutableStateOf(production.rate)
    }
    var watchedStatus by remember {
        mutableStateOf(production.isWatched)
    }
    var imageUri by remember {
        mutableStateOf(production.imageUri)
    }
    var title by remember {
        mutableStateOf(production.title)
    }
    var releaseDate by remember {
        mutableStateOf(production.releaseDate)
    }
    var genre by remember {
        mutableStateOf(production.genre)
    }
    var openChooseGenre by remember {
        mutableStateOf(false)
    }
    var durationOrParts by remember {
        mutableStateOf(
            when (production) {
                is Movie -> production.durationInMinutes
                is Series -> production.parts.keys.size
                else -> 0
            }
        )
    }

    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    val watchedScopeInfo = stringResource(id = R.string.watched_scope_info)
    val watchedScopeWarning = stringResource(id = R.string.watched_scope_warning)

    if (production == null) {
        NotFound()
    } else {
        if (!watchedStatus) {
            LaunchedEffect(watchedScopeInfo) {
                hostState.showSnackbar(
                    message = watchedScopeInfo,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        if (openChooseGenre) {
                            openChooseGenre = false
                        }
                    })
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f
                        )
                    )
                    .padding(bottom = 100.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        ImageSection(
                            production = production,
                            watchedStatus = watchedStatus,
                            title = title,
                            onTitleChange = { newTitle ->
                                title = newTitle
                                production.title = newTitle
                            },
                            onReleaseDateChange = { newRelease ->
                                releaseDate = newRelease
                                production.releaseDate = newRelease
                            },
                            genre = genre,
                            openChooseGenre = openChooseGenre,
                            onOpenGenreChooseChange = { newOpenGenreChoose ->
                                openChooseGenre = newOpenGenreChoose
                            },
                            onDurationOrPartsChange = { newDurationOrParts ->
                                durationOrParts = newDurationOrParts
                                when (production) {
                                    is Movie -> production.durationInMinutes = newDurationOrParts
                                    is Series -> production.parts[newDurationOrParts] = 0
                                }
                            },
                            image = Uri.parse(imageUri),
                            onImageSelected = { newUri ->
                                imageUri = newUri.toString()
                                production.imageUri = newUri.toString()
                            },
                            scope = scope,
                            hostState = hostState,
                            scopeMessage = watchedScopeWarning,
                        )
                    }
                    item {
                        GenreChooseSection(
                            openGenreChoose = openChooseGenre,
                            genres = genres,
                            onGenreChange = { newGenre ->
                                genre = newGenre
                                production.genre = newGenre
                            },
                            watchedStatus = watchedStatus,
                            actualGenre = genre,
                        )
                    }
                    item {
                        WatchedStatusSection(
                            watchedStatus = watchedStatus,
                            onWatchedStatusChange = { newStatus ->
                                watchedStatus = newStatus
                                production.isWatched = newStatus
                            }
                        )
                    }
                    item {
                        CommentSection(
                            watchedStatus = watchedStatus,
                            comment = comment!!,
                            onCommentChange = { newComment ->
                                comment = newComment
                                production.comment = newComment.trim()
                            }
                        )
                    }
                    item {
                        RateSection(
                            watchedStatus = watchedStatus,
                            rating = rating!!,
                            onRatingChange = { newRating ->
                                rating = newRating
                                production.rate = newRating
                            }
                        )
                    }
                }
            }
            SaveButtonSection(
                onClick = { saveProductions(context, productions) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier
                .padding(top = 32.dp)
        )
    }
}

@Composable
fun NotFound() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.four),
            fontSize = 128.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(id = R.string.zero),
            fontSize = 128.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(id = R.string.four),
            fontSize = 128.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
        )
    }
}

fun Long?.toLocalDate(): LocalDate? {
    return this?.let { millis ->
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

fun LocalDate.toMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    openDialog: Boolean,
    onOpenDialogChange: (Boolean) -> Unit,
    dateState: DatePickerState,
    onReleaseDateChange: (LocalDate) -> Unit
) {
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenDialogChange(false) },
            confirmButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.48f),
                    onClick = {
                        onOpenDialogChange(false)
                        if (dateState.selectedDateMillis != null) {
                            onReleaseDateChange(dateState.selectedDateMillis.toLocalDate()!!)
                        }
                    },
                    colors = ButtonColors(
                        containerColor = DarkPurple,
                        contentColor = TextH1,
                        disabledContainerColor = LightPurple,
                        disabledContentColor = TextH2,
                    )
                ) {
                    Text(text = stringResource(id = R.string.calendar_confirm))
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.48f),
                    onClick = { onOpenDialogChange(false) },
                    colors = ButtonColors(
                        containerColor = DarkPurple,
                        contentColor = TextH1,
                        disabledContainerColor = LightPurple,
                        disabledContentColor = TextH2,
                    )
                ) {
                    Text(text = stringResource(id = R.string.calendar_dismiss))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = DarkGray,
            )
        ) {
            DatePicker(
                state = dateState,
                colors = DatePickerDefaults.colors(
                    containerColor = DarkGray,
                    titleContentColor = TextH1,
                    headlineContentColor = TextH1,
                    weekdayContentColor = TextH1,
                    subheadContentColor = TextH1,
                    navigationContentColor = TextH1,
                    yearContentColor = TextH1,
                    currentYearContentColor = TextH1,
                    selectedYearContentColor = TextH1,
                    selectedYearContainerColor = DarkPurple,
                    dayContentColor = TextH1,
                    selectedDayContentColor = TextH1,
                    selectedDayContainerColor = DarkPurple,
                    dayInSelectionRangeContentColor = TextH1,
                    dayInSelectionRangeContainerColor = DarkPurple,
                    dividerColor = DarkGray,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSection(
    production: Production,
    watchedStatus: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    genre: Genre,
    openChooseGenre: Boolean,
    onOpenGenreChooseChange: (Boolean) -> Unit,
    onReleaseDateChange: (LocalDate) -> Unit,
    onDurationOrPartsChange: (Int) -> Unit,
    image: Uri,
    onImageSelected: (Uri) -> Unit,
    scope: CoroutineScope,
    hostState: SnackbarHostState,
    scopeMessage: String,
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onImageSelected(uri ?: image)
        },
    )
    var openDialog by remember { mutableStateOf(false) }
    var dateState = rememberDatePickerState(
        initialSelectedDateMillis = production.releaseDate.toMillis(),
        yearRange = 1900..LocalDate.now().year + 2
    )
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        DatePickerDialog(
            openDialog = openDialog,
            onOpenDialogChange = { newOpenDialog ->
                openDialog = newOpenDialog
            },
            dateState = dateState,
            onReleaseDateChange = { newReleaseDate ->
                onReleaseDateChange(newReleaseDate)
            }
        )
        if (production.imageUri != null) {
            production.imageUri?.let { imageUri ->
                AsyncImage(
                    model = imageUri,
                    contentDescription = production.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            if (watchedStatus) {
                                scope.launch {
                                    hostState.showSnackbar(
                                        message = scopeMessage,
                                    )
                                }
                            } else {
                                imagePicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        }
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
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 38.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    BasicTextField(
                        enabled = !watchedStatus,
                        value = title.uppercase(),
                        onValueChange = onTitleChange,
                        textStyle = TextStyle(
                            color = TextH1,
                            fontSize = 32.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .background(Color.Transparent)
                            .clickable {
                                if (watchedStatus) {
                                    scope.launch {
                                        hostState.showSnackbar(
                                            message = scopeMessage,
                                        )
                                    }
                                }
                            }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = production.releaseDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextH2,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (watchedStatus) {
                                        scope.launch {
                                            hostState.showSnackbar(
                                                message = scopeMessage,
                                            )
                                        }
                                    } else {
                                        openDialog = !openDialog
                                    }
                                }
                        )
                        Text(
                            text = genre.name.lowercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextH2,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (watchedStatus) {
                                        scope.launch {
                                            hostState.showSnackbar(
                                                message = scopeMessage,
                                            )
                                        }
                                    } else {
                                        onOpenGenreChooseChange(!openChooseGenre)
                                    }
                                }
                        )
                        when (production) {
                            is Movie -> {
                                Text(
                                    text = "${production.durationInMinutes / 60}h " +
                                            "${production.durationInMinutes % 60}min",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextH2
                                )
                            }

                            is Series -> {
                                Text(
                                    text = "${production.parts.keys.size} s." +
                                            " ${production.parts.values.sumOf { it }} e.",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextH2
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 12.dp,
                        color = TextH2,
                    )
                    .clickable {
                        //TO DO: DODANIE ZDJECIA
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "production image",
                    tint = TextH2,
                    modifier = Modifier
                        .size(128.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreChooseSection(
    openGenreChoose: Boolean,
    actualGenre: Genre,
    genres: List<Genre>,
    onGenreChange: (Genre) -> Unit,
    watchedStatus: Boolean,
) {
    val genres = genres.filter { it.name != "ALL" }
    if (openGenreChoose && !watchedStatus) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(genres.size) { index ->
                GenreChip(
                    genre = genres[index],
                    modifier = Modifier
                        .clickable { onGenreChange(genres[index]) },
                    actualGenre = actualGenre
                )
            }
        }
    }
}

@Composable
fun GenreChip(
    modifier: Modifier = Modifier,
    genre: Genre,
    actualGenre: Genre
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (genre == actualGenre) DarkPurple
                else DarkGray
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextH1,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                )
        )
    }
}

@Composable
fun CommentSection(
    comment: String,
    onCommentChange: (String) -> Unit,
    watchedStatus: Boolean,
) {
    if (watchedStatus) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(id = R.string.my_comment),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextH1,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    TextField(
                        value = comment,
                        onValueChange = onCommentChange,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DarkGray,
                            unfocusedContainerColor = DarkGray,
                            focusedTextColor = TextH2,
                            unfocusedTextColor = TextH2,
                            cursorColor = TextH2,
                            focusedIndicatorColor = DarkGray,
                            unfocusedIndicatorColor = DarkGray,
                            disabledContainerColor = DarkGray,
                            disabledTextColor = TextH2,
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun RateSection(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    watchedStatus: Boolean,
) {
    if (watchedStatus) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.rate),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextH1,
                modifier = Modifier
                    .padding(end = 100.dp)
            )
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < rating) Icons.Filled.StarRate else Icons.Outlined.StarRate,
                    contentDescription = "production rate",
                    tint = if (index < rating) TextH1 else TextH2,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable {
                            onRatingChange(index + 1)
                        }
                )
            }
        }
    }
}

@Composable
fun IsWatchedChip(
    chipText: String,
    watchedStatus: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (watchedStatus) DarkPurple else DarkGray
            )
            .padding(8.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = chipText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextH1,
        )
    }
}

@Composable
fun WatchedStatusSection(
    onWatchedStatusChange: (Boolean) -> Unit,
    watchedStatus: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IsWatchedChip(
            chipText = stringResource(id = R.string.not_watched),
            watchedStatus = !watchedStatus,
            onClick = { onWatchedStatusChange(false) }
        )
        IsWatchedChip(
            chipText = stringResource(id = R.string.watched),
            watchedStatus = watchedStatus,
            onClick = { onWatchedStatusChange(true) }
        )
    }
}


@Composable
fun SaveButtonSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 32.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { onClick() },
            shape = RoundedCornerShape(22.dp),
            enabled = true,
            contentPadding = PaddingValues(12.dp),
            colors = ButtonColors(
                contentColor = TextH1,
                containerColor = DarkPurple,
                disabledContentColor = TextH2,
                disabledContainerColor = LightPurple,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.save_button_text).uppercase(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}




















