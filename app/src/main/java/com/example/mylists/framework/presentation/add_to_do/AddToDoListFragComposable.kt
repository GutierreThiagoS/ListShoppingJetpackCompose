package com.example.mylists.framework.presentation.add_to_do

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylists.framework.presentation.add.OutlinedEditText

@Composable
fun AddToDoList() {

    var titleValue by rememberSaveable { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                text = "Registrar Tarefa!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            OutlinedEditText(
                label = "Titulo",
            ) { text ->
                titleValue = text
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedEditText(
                label = "Descrição",
            ) { text ->
                titleValue = text
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedEditText(
                label = "Data Final",
            ) { text ->
                titleValue = text
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { }) {
                Text(text = "Adicionar")
            }
        }
    }
}