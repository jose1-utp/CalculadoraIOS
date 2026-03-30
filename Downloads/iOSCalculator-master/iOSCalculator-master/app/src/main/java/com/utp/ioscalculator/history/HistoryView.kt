package com.utp.ioscalculator.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.utp.ioscalculator.calculator.CalculatorViewModel

@Composable
fun HistoryView(
    viewModel: HistoryViewModel = viewModel(),
    navController: NavController
) {
    val history = viewModel.history
    val viewModel: HistoryViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Button(
            onClick = { navController.navigate("calculator") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF181818),
                contentColor = Color.White
            ),
            shape = CircleShape,
            modifier = Modifier
                .padding(end = 10.dp, top = 20.dp)
                .size(60.dp)
                .align(Alignment.TopEnd)
        ) {
            Text(
                text = "X",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay operaciones aún",
                    color = Color.Gray,
                    fontSize = 30.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top= 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(history.reversed()) { item ->
                    HistoryItem(item)
                }
            }
        }



    }
}


@Composable
private fun HistoryItem(entry: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
    ) {
        Text(
            text = entry,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}
@Preview
@Composable
fun Test(){
    val navController = rememberNavController()
    HistoryView(
        navController = navController
    )
}
