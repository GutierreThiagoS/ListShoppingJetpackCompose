package com.example.mylists.framework.presentation.add_to_do

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mylists.framework.composable.DateSelector
import com.example.mylists.framework.presentation.add.OutlinedEditText
import com.example.mylists.framework.presentation.to_do.ToDoViewModel
import com.example.mylists.framework.utils.dateFormat
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddToDoList(viewModel: ToDoViewModel = koinViewModel()) {

    var titleValue by rememberSaveable { mutableStateOf("") }
    var descriptionValue by rememberSaveable { mutableStateOf("") }
    var dateFinalValue by remember { mutableStateOf(TextFieldValue()) }
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }

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
                textValue = titleValue
            ) { text ->
                titleValue = text
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedEditText(
                label = "Descrição",
                textValue = descriptionValue
            ) { text ->
                descriptionValue = text
            }
            Spacer(modifier = Modifier.height(16.dp))

            DateSelector {
                dateFinalValue = it
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .toggleable(
                        value = checkedState,
                        onValueChange = { onStateChange(!checkedState) },
                        role = Role.Checkbox
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(Color.Red)
                )
                Text(
                    text = "Alertar",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Button(
                onClick = {
                    if (
                        titleValue.isNotBlank() &&
                        descriptionValue.isNotBlank() &&
                        dateFinalValue.text.isNotBlank() &&
                        dateFormat.parse(dateFinalValue.text) != null
                    ) {
        //                    viewModel.insertToDo()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save To Do")
                Text(text = "Adicionar")
            }
        }
    }
}