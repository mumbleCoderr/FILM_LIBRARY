package com.example.filmlibrary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.filmlibrary.data.Production
import com.example.filmlibrary.data.getProductions
import com.example.filmlibrary.ui.theme.LightPurple
import com.example.filmlibrary.ui.theme.TextH1

@Composable
fun ProductionDetailsScreen(productionTitle: String?){
    val context = LocalContext.current
    val production = getProductions(context).find {
        it.title == productionTitle
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPurple)
    ){
        Text(
            text = production.toString(),
            fontSize = 64.sp,
            color = TextH1
        )
    }
}