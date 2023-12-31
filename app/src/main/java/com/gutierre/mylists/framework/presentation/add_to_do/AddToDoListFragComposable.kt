package com.gutierre.mylists.framework.presentation.add_to_do

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.framework.composable.DateSelector
import com.gutierre.mylists.framework.composable.HourSelectorAlert
import com.gutierre.mylists.framework.presentation.add.OutlinedEditText
import com.gutierre.mylists.framework.presentation.to_do.ToDoViewModel
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.utils.dateFormat
import com.gutierre.mylists.framework.utils.hourFormat
import com.gutierre.mylists.framework.utils.logE
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddToDoList(
    mainViewModel: MainViewModel,
    viewModel: ToDoViewModel = koinViewModel()
) {

    val toDoItem by mainViewModel.toDoState.collectAsState()

    var titleValue by rememberSaveable { mutableStateOf(toDoItem?.title ?: "") }

    var descriptionValue by rememberSaveable { mutableStateOf(toDoItem?.description ?: "") }

    var dateFinalValue by remember { mutableStateOf(TextFieldValue(toDoItem?.dateFinal ?: "")) }

    var hourAlertValue by remember { mutableStateOf(TextFieldValue(toDoItem?.hourInitAlert ?: "")) }

    var intervalValue by rememberSaveable { mutableStateOf("${toDoItem?.extendTimer ?: "15"}") }

    val (checkedState, onStateChange) = remember { mutableStateOf(toDoItem?.alert ?: true) }

    val activity = LocalView.current.context as AppCompatActivity
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

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
                text = "${if (toDoItem != null) "Editar" else "Registrar"} Tarefa!",
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

            DateSelector(
                toDoItem?.dateFinal,
                activity = activity
            ) {
                dateFinalValue = it
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Column(modifier = Modifier.weight(3f)) {
                    HourSelectorAlert(
                        toDoItem?.hourInitAlert,
                        activity = activity
                    ) {
                        hourAlertValue = it
                    }
                }

                OutlinedEditText(
                    modifier = Modifier.weight(1f),
                    label = "Intervalo",
                    textValue = intervalValue,
                    keyboardType = KeyboardType.Number
                ) { text ->
                    intervalValue = text
                }
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

            OutlinedButton(
                onClick = {
                    if (
                        titleValue.isNotBlank() &&
                        descriptionValue.isNotBlank() &&
                        intervalValue.isNotBlank() &&
                        dateFinalValue.text.isNotBlank() &&
                        dateFormat.parse(dateFinalValue.text) != null &&
                        hourAlertValue.text.isNotBlank() &&
                        hourFormat.parse(hourAlertValue.text) != null
                    ) {
                        viewModel.insertOrUpdateToDoItem(
                            itemOld = toDoItem,
                            title = titleValue,
                            description = descriptionValue,
                            dateFinal = dateFinalValue.text,
                            hourAlertValue = hourAlertValue.text,
                            isAlert = checkedState,
                            interval = intervalValue,
                            returnError = {
                                logE("Error insertToDo $it")
                                Toast.makeText(
                                    activity,
                                    "Lembrete não Salvo!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            returnSuccess = {
                                logE("OK returnSuccess OK")
                                mainViewModel.setToDoEdit(null)
                                dispatcher?.onBackPressed()
                            }
                        )
                    } else Toast.makeText(
                        activity, "Algum campo está vázio!", Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save To Do"
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "Adicionar")
            }
        }
    }
}