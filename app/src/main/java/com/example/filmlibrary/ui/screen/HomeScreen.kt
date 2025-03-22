package com.example.filmlibrary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.filmlibrary.R
import com.example.filmlibrary.data.Genre
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.ui.theme.DarkPink
import com.example.filmlibrary.ui.theme.DarkPurple
import com.example.filmlibrary.ui.theme.LightPink
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1
import com.example.filmlibrary.ui.theme.TextH2

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .background(DarkPurple)
            .fillMaxSize()

    ) {
        Column {
            TopBar()
            FilterChips()
            SearchBar()
        }

    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, 52.dp),
    ) {
        Text(
            text = stringResource(id = R.string.title1),
            color = TextH1,
            fontSize = 25.sp,
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
fun FilterChips(){
    var selectedFilter by remember{
        mutableStateOf(0)
    }

    val genres = Genre.entries

    LazyRow {
        items(genres.size){ index ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 32.dp,
                        bottom = 16.dp,
                        end = if (index == genres.size - 1) 16.dp else 0.dp
                    )
                    .clickable {
                        selectedFilter = index
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if(selectedFilter == index) DarkPink
                        else LightPurple
                    )
                    .height(48.dp)
                    .padding(12.dp)
            ){
                Text(
                    text = genres[index].name,
                    color = TextH1,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SearchBar() {
    var input by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = 16.dp
            )
    ) {
        BasicTextField(
            value = input,
            onValueChange = { newInput -> input = newInput },
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    color = TextH2,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 2.dp),
            textStyle = LocalTextStyle.current.copy(
                color = TextH1,
                fontSize = 18.sp,
                lineHeight = 32.sp
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Alignment.CenterStart
                ) {
                    if (input.isEmpty()) {
                        Row() {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                modifier = Modifier
                                    .size(48.dp),
                                tint = TextH1,
                            )
                            Text(
                                text = "Search",
                                color = TextH1,
                                fontSize = 18.sp,
                                lineHeight = 32.sp
                            )
                        }
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun ProductionList(){

}

















fun String.limit(maxLength: Int): String {
    return if (this.length > maxLength) this.take(maxLength) + "..." else this
}

@Composable
fun ProductionLayout(production: Production, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(350.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFF4A148C)),
        Alignment.Center
    ) {
        Text(
            text = production.title.uppercase().limit(14),
            color = Color(0xFFefe5fd),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun AddButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(100.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF4A148C)),
        Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFFefe5fd)
        )
    }
}

@Composable
fun ProductionList(productions: List<Production>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            itemsIndexed(productions) { index, item ->
                ProductionLayout(
                    production = item,
                    Modifier
                        .padding(vertical = 8.dp)
                )
            }
            item {
                AddButton(
                    Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}