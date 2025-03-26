package com.example.filmlibrary.ui.screen

import android.content.Context
import android.content.pm.ModuleInfo
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.filmlibrary.R
import com.example.filmlibrary.Screen
import com.example.filmlibrary.data.Movie
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.data.Series
import com.example.filmlibrary.data.loadProductions
import com.example.filmlibrary.data.saveProductions
import com.example.filmlibrary.ui.theme.DarkGray
import com.example.filmlibrary.ui.theme.DarkPurple
import com.example.filmlibrary.ui.theme.LightPink
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1
import com.example.filmlibrary.ui.theme.TextH2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun ProductionDetailsScreen(productionTitle: String?) {
    val context = LocalContext.current
    var productions by remember {
        mutableStateOf(loadProductions(context) ?: emptyList())
    }

    val production = productions.find {
        it.title == productionTitle
    }
    var comment by remember {
        mutableStateOf(production?.comment ?: "")
    }
    var rating by remember {
        mutableStateOf(production?.rate ?: 0)
    }
    var watchedStatus by remember {
        mutableStateOf(production?.isWatched ?: false)
    }

    val message = stringResource(id = R.string.watched_scope_effect)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (production == null) {
        NotFound()
    } else {
        if(production.isWatched){
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
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
                    item { ImageSection(production) }
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
                            comment = comment,
                            onCommentChange = { newComment ->
                                comment = newComment
                                production.comment = newComment.trim()
                            }
                        )
                    }
                    item {
                        RateSection(
                            watchedStatus = watchedStatus,
                            rating = rating,
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
            hostState = snackbarHostState,
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

@Preview
@Composable
fun ImageSection(production: Production) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        if (production.imageUri != null) {
            production.imageUri?.let { imageUri ->
                AsyncImage(
                    model = imageUri,
                    contentDescription = production.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
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
                    Text(
                        text = production.title.uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextH1,
                        lineHeight = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
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
                        )
                        Text(
                            text = production.genre.name.lowercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextH2,
                            modifier = Modifier
                                .padding(end = 16.dp)
                        )
                        when (production) {
                            is Movie -> {
                                Text(
                                    text = "${production.durationInMinutes / 60}h ${production.durationInMinutes % 60}min",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextH2
                                )
                            }

                            is Series -> {
                                Text(
                                    text = "${production.parts.keys.size} seasons",
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
    if(watchedStatus) {
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
fun SaveButtonSection(onClick: () -> Unit, modifier: Modifier = Modifier) {
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




















