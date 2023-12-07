package com.gutierre.mylists.framework.presentation.configuration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.R
import com.gutierre.mylists.framework.composable.LineDivided
import com.gutierre.mylists.domain.model.Category
import com.gutierre.mylists.framework.ui.theme.Teal10
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfigurationFrag(viewModel: ConfigurationViewModel = koinViewModel()) {

    val categoryList by viewModel.categoryList.collectAsState(listOf())

    val isUserSupport by viewModel.isUserSupport.collectAsState()

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    if (isUserSupport) {
        Surface {
            Column{
                Card(modifier = Modifier.padding(5.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Teal10)
                            .padding(start = 20.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(150.dp),
                                painter = painterResource(id = R.mipmap.support),
                                contentDescription = "Imagem"
                            )
                        }

                        Spacer(modifier = Modifier.padding(20.dp))

                        val textName = remember {
                            mutableStateOf("")
                        }

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Nome")},
                            value = textName.value,
                            onValueChange = {
                                textName.value = it
                        })

                        Spacer(modifier = Modifier.padding(10.dp))

                        val textTitle = remember {
                            mutableStateOf("")
                        }

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Titulo")},
                            value = textTitle.value,
                            onValueChange = {
                                textTitle.value = it
                        })
                        Spacer(modifier = Modifier.padding(10.dp))

                        val textMessage = remember {
                            mutableStateOf("")
                        }

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Messagem")},
                            value = textMessage.value,
                            minLines = 6,
                            maxLines = 50,
                            onValueChange = {
                                textMessage.value = it
                        })
                        Spacer(modifier = Modifier.padding(10.dp))

                        Button(onClick = { viewModel.setUserSupport(false) }) {
                            Icon(imageVector = Icons.Outlined.Send, contentDescription = "SEND")
                            Text(text = "Enviar")
                        }

                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)) {
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
            ){
                Card(modifier = Modifier.padding(5.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Teal10)
                            .padding(start = 20.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .clip(
                                        MaterialTheme.shapes.small.copy(
                                            CornerSize(100.dp)
                                        )
                                    )
                                    .background(Color.White)
                                    .size(150.dp)
                                    .padding(
                                        start = 30.dp,
                                        top = 20.dp,
                                        end = 20.dp,
                                        bottom = 20.dp
                                    ),
                                painter = painterResource(id = R.mipmap.user_settings),
                                contentDescription = "Imagem"
                            )
                        }

                        Text(
                            text = "Desenvolvido por Gutierre Guimarães",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Versão: ${context.packageManager.getPackageInfo(context.packageName, 0).versionName}"
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(
                            modifier =
                            Modifier
                                .clip(
                                    MaterialTheme.shapes.small.copy(
                                        CornerSize(8.dp)
                                    )
                                )
                                .width(120.dp)
                                .background(Color.White)
                                .padding(10.dp)
                                .clickable {
                                    val url =
                                        "https://www.linkedin.com/in/gutierre-thiago-guimar%C3%A3es-73a1b9119/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
                                    uriHandler.openUri(url)
                                }
                            ,
                            painter = painterResource(id = R.mipmap.linkedin_logo),
                            contentDescription = "LinkedIn"
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                            .clickable {
                                viewModel.setUserSupport(true)
                            }

                    )  {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .height(200.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.height(100.dp),
                                painter = painterResource(id = R.mipmap.support),
                                contentDescription = "Support"
                            )
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "Suporte",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .height(200.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier
                                    .height(100.dp)
                                    .clip(
                                        MaterialTheme.shapes.small.copy(
                                            CornerSize(100.dp)
                                        )
                                    )
                                    .background(Teal10)
                                    .padding(30.dp),
                                painter = painterResource(id = R.mipmap.editar_category),
                                contentDescription = "Editar Categoria"
                            )
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "Editar Categoria",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (categoryList.isNotEmpty()) {
                    CategoriesListLayout(categoryList)
                }
            }
        }
    }

}

@Composable
fun CategoriesListLayout(categoryList: List<Category>) {
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

