package com.example.mylists.framework.presentation.to_do

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ToDoListFrag(
    viewModel: ToDoViewModel = koinViewModel()
) {
    val toDoListState by viewModel.toDoListState.collectAsState(listOf())

    if (toDoListState.isNotEmpty()) {
        LazyColumn {
            itemsIndexed(toDoListState) { index, toDoItem ->
                Card(modifier = Modifier
                    .fillMaxWidth().let {
                        if (index == 0) {
                            it.padding(4.dp)
                        } else it
                            .padding(bottom = 4.dp)
                            .padding(horizontal = 4.dp)
                    }) {
                    Column {
                        Text(
                            text = toDoItem.title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = toDoItem.description,
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Não há Lembrete!\nRegistre Aqui",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Icon(
                modifier = Modifier.height(200.dp),
                imageVector = Icons.Outlined.ArrowDownward,
                contentDescription = "ArrowDropDown",
                tint = Color.DarkGray
            )
        }
    }
}
