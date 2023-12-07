package com.gutierre.mylists.framework.presentation.to_do

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gutierre.mylists.NavigationScreen
import com.gutierre.mylists.domain.model.ToDoItem
import com.gutierre.mylists.framework.composable.SegmentedControl
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.ui.theme.Bordor
import org.koin.androidx.compose.koinViewModel

@Composable
fun ToDoListFrag(
    viewModel: ToDoViewModel = koinViewModel(),
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val toDoListState by viewModel.toDoListState.collectAsState(listOf())

    val filterList = listOf("Todos", "Pendente", "Concluído", "Lixeira")

    val index = remember {
        mutableIntStateOf(0)
    }

    if (toDoListState.isNotEmpty()) {
        Column {
            SegmentedControl(items = filterList, onItemSelection = {
                index.intValue = it
            })
            LazyColumn {
                itemsIndexed(toDoListState.filter {
                    when (index.intValue) {
                        1 -> !it.concluded && !it.deleted
                        2 -> it.concluded && !it.deleted
                        3 -> it.deleted
                        else -> !it.deleted
                    }
                }) { index, toDoItem ->
                    ItemToDoComposable(index, toDoItem, editToDoItem = {
                        mainViewModel.setToDoEdit(it)
                        mainViewModel.setNavigation(NavigationScreen.ADD_TO_DO.label)
                        navController.navigate(NavigationScreen.ADD_TO_DO.label)
                    }, deleteToDoItem = {
                        if (it.deleted) {
                            viewModel.deleteToDoItem(it)
                        } else viewModel.deleteToDoItemId(it.id)
                    })
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

@Composable
fun ItemToDoComposable(
    index: Int,
    toDoItem: ToDoItem,
    editToDoItem: (ToDoItem) -> Unit,
    deleteToDoItem: (ToDoItem) -> Unit
) {

    val expanded = remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth().let {
            if (index == 0) {
                it.padding(4.dp)
            } else it
                .padding(bottom = 4.dp)
                .padding(horizontal = 4.dp)
        }) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(start = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    if (toDoItem.alert || toDoItem.concluded) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 20.dp, end = 10.dp),
                            imageVector = if (toDoItem.deleted) Icons.Outlined.Delete
                            else if (toDoItem.concluded) Icons.Outlined.BookmarkAdded
                            else Icons.Outlined.Alarm,
                            contentDescription = "Alarm",
                            tint = if (toDoItem.deleted) Bordor
                            else if (toDoItem.concluded) Color.Green
                            else Color.Red
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = toDoItem.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                }

                IconButton(
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More"
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                expanded.value = false
                                editToDoItem(toDoItem)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (toDoItem.deleted) "Deletar" else "Remover") },
                            onClick = {
                                expanded.value = false
                                deleteToDoItem(toDoItem)
                            }
                        )
                    }
                }
            }

            Text(
                text = toDoItem.description,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                modifier = Modifier.align(Alignment.End).padding(end = 10.dp, bottom = 5.dp),
                text = "${toDoItem.dateFinal} ${toDoItem.hourInitAlert} ${
                    if (toDoItem.hourInitAlert != toDoItem.hourAlert) "... " + toDoItem.hourAlert else ""
                }",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Gray
                )
            )
        }
    }
}