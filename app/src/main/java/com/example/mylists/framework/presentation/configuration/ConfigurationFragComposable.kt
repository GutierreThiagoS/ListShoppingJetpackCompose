package com.example.mylists.framework.presentation.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylists.composable.LineDivided
import com.example.mylists.domain.model.Category
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfigurationFrag(viewModel: ConfigurationViewModel = koinViewModel()) {

    val categoryList by viewModel.categoryList.collectAsState(listOf())

    if (categoryList.isNotEmpty()) {
        CategoriesListLayout(categoryList)
    }
}

@Composable
fun CategoriesListLayout(categoryList: List<Category>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)) {
        OutlinedCard(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp),
                text = "Categorias",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .padding(4.dp)
                ) {
                    itemsIndexed(categoryList) { i, category ->
                        CategoryItem(category = category, index = i)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    index: Int,
    viewModel: ConfigurationViewModel = koinViewModel()
) {

    val openDialogCustom = remember { mutableStateOf(false) }
    val openDialogSample = remember { mutableStateOf(false) }
    val openDialogMessage = remember { mutableStateOf("") }

    Column {
        if (index != 0 ) LineDivided()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 16.dp),
                text = category.nameCategory,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row {
                IconButton(onClick = {  }) {
                    Icon(imageVector = Icons.Outlined.Edit , contentDescription = "Icone Edit Categoria")
                }
                IconButton(onClick = { openDialogCustom.value = true }) {
                    Icon(imageVector = Icons.Outlined.Delete , contentDescription = "Icone Deletar Categoria")
                }
            }
        }
    }

    if (openDialogCustom.value) {
        AlertDialogCustom(
            message = "Deseja Realmente Remover a categoria '${category.nameCategory}'",
            openDialog = openDialogCustom.value,
            onDismiss = { openDialogCustom.value = false },
            confirmButton = {
                viewModel.removeCategory(category) {
                    if (it.isNotBlank()) {
                        openDialogMessage.value = it
                        openDialogSample.value = true
                    }
                }
                openDialogCustom.value = false
            },
            negationButton = { openDialogCustom.value = false }
        )
    }

    if (openDialogSample.value) {
        AlertDialogSample(
            message = openDialogMessage.value,
            openDialog = openDialogSample.value
        ) { openDialogSample.value = false }
    }
}

@Composable
fun AlertDialogCustom(
    message: String,
    title: String = "Atenção",
    openDialog: Boolean = false,
    onDismiss: () -> Unit,
    confirmButton: () -> Unit,
    negationButton: () -> Unit
) {

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                onDismiss()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmButton()
                    }
                ) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        negationButton()
                    }
                ) {
                    Text("Não")
                }
            }
        )
    }
}

@Composable
fun AlertDialogSample(
    message: String,
    title: String = "Atenção",
    openDialog: Boolean = false,
    onDismiss: () -> Unit
) {

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Ok")
                }
            }
        )
    }
}

