package com.gutierre.mylists.framework.presentation.configuration

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.SaveAs
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.R
import com.gutierre.mylists.framework.composable.LineDivided
import com.gutierre.mylists.domain.model.CategoryAndCountProduct
import com.gutierre.mylists.framework.ui.theme.Teal10
import com.gutierre.mylists.framework.ui.theme.VeryLightGray
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfigurationFrag(viewModel: ConfigurationViewModel = koinViewModel()) {

    val categoryList by viewModel.categoryList.collectAsState(listOf())

    val isUserSupport by viewModel.isUserSupport.collectAsState()

    val isEditCategory by viewModel.isEditCategory.collectAsState()

    when {
        isUserSupport -> SendEmailSupport(viewModel)
        isEditCategory -> {
            if (categoryList.isNotEmpty())
                CategoriesListLayout(categoryList, viewModel)
            else
                Text(text = "Vazio")
        }
        else -> ConfigurationLayout(categoryList, viewModel)
    }
}

@Composable
fun ConfigurationLayout(categoryList: List<CategoryAndCountProduct>, viewModel: ConfigurationViewModel) {

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)) {
        Column(
            modifier = Modifier
                .background(VeryLightGray)
        ){
            Card(modifier = Modifier.padding(5.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
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
                                .background(Teal10)
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
                            .clickable {
                                val url =
                                    "https://www.linkedin.com/in/gutierre-thiago-guimar%C3%A3es-73a1b9119/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
                                uriHandler.openUri(url)
                            }
                            .border(
                                BorderStroke(1.dp, Color.LightGray),
                                MaterialTheme.shapes.small.copy(
                                    CornerSize(8.dp)
                                )
                            )
                            .width(120.dp)
                            .padding(10.dp)
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
                        .clickable {
                            if (categoryList.isNotEmpty()) {
                                viewModel.setEditCategory(true)
                            } else Toast
                                .makeText(context, "Não existe Categoria", Toast.LENGTH_SHORT)
                                .show()
                        }
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
        }
    }
}

@Composable
fun CategoriesListLayout(categoryList: List<CategoryAndCountProduct>, viewModel: ConfigurationViewModel) {

    val newCategory = remember {
        mutableStateOf(false)
    }

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (modifier = Modifier
            .background(VeryLightGray)
        ) {
            OutlinedCard(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(10f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp),
                        text = "Categorias",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )


                    ButtonCustom(
                        text = if (newCategory.value) "Cancelar" else "Novo",
                        icon = if (newCategory.value) Icons.Outlined.Cancel else Icons.Outlined.PostAdd,
                        clickable = {
                            newCategory.value = !newCategory.value
                        }
                    )
                }


                Column {
                    if (newCategory.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val textCategory = remember {
                                mutableStateOf("")
                            }

                            OutlinedTextField(
                                value = textCategory.value,
                                onValueChange = {
                                    textCategory.value = it
                                },
                                label = { Text(text = "Categoria") }
                            )
                            ButtonCustom(
                                text = "Salvar",
                                icon = Icons.Outlined.Save,
                                clickable = {
                                }
                            )
                        }
                        LineDivided()
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .heightIn(max = 250.dp)
                            .padding(4.dp)
                    ) {
                        itemsIndexed(categoryList) { i, category ->
                            CategoryItem(category = category, index = i, viewModel)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { viewModel.setEditCategory(false) }) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryAndCountProduct,
    index: Int,
    viewModel: ConfigurationViewModel
) {

    val context = LocalContext.current

    val openDialogCustom = remember { mutableStateOf(false) }
    val openDialogSample = remember { mutableStateOf(false) }
    val openDialogMessage = remember { mutableStateOf("") }

    val editCategory = remember {
        mutableStateOf(false)
    }

    Column {
        if (index != 0 ) LineDivided()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textName = remember {
                mutableStateOf(category.nameCategory)
            }
            if (editCategory.value) {
                OutlinedTextField(
                    value = textName.value,
                    onValueChange = {
                        textName.value = it
                    },
                    label = { Text(text = "Novo Nome") }
                )
            } else Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 16.dp),
                text =  "${category.nameCategory} - ${category.countProduct}",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
            )

            Row {
                if (editCategory.value) IconButton(onClick = {
                    viewModel.editCategory(textName.value, category = category, context = context)
                }) {
                    Icon(imageVector = Icons.Outlined.SaveAs, contentDescription = "Salvar Edicao")
                }
                IconButton(onClick = { editCategory.value = !editCategory.value }) {
                    Icon(imageVector = if (editCategory.value) Icons.Outlined.Cancel else Icons.Outlined.Edit , contentDescription = "Icone Edit Categoria")
                }
                if (!editCategory.value) IconButton(onClick = { openDialogCustom.value = true }) {
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
fun SendEmailSupport(viewModel: ConfigurationViewModel) {

    val context = LocalContext.current

    Surface {
        Card(modifier = Modifier.padding(5.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 20.dp, end = 20.dp),
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

                val textNome = remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = textNome.value,
                    onValueChange = {
                        textNome.value = it
                    },
                    label = { Text(text = "Nome") }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                val textTitle = remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Assunto")},
                    value = textTitle.value,
                    onValueChange = {
                        textTitle.value = it
                    })
                Spacer(modifier = Modifier.padding(10.dp))

                val textMessage = remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Messagem")},
                    value = textMessage.value,
                    minLines = 6,
                    maxLines = 50,
                    onValueChange = {
                        textMessage.value = it
                    })
                Spacer(modifier = Modifier.padding(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                    OutlinedButton(
                        onClick = { viewModel.setUserSupport(false) }
                    ) {
                        Text(text = "Cancelar")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedButton(
                        onClick = {
                            if (
                                textNome.value.isNotBlank() &&
                                textTitle.value.isNotBlank() &&
                                textMessage.value.isNotBlank()
                            ) {
                                val intent = Intent(Intent.ACTION_SENDTO)
                                intent.data = Uri.parse("mailto:")
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gutierrethiagodev@gmail.com"))
                                intent.putExtra(Intent.EXTRA_SUBJECT, textTitle.value)
                                intent.putExtra(Intent.EXTRA_TEXT, textMessage.value)

                                context.startActivity(intent)
                            } else Toast.makeText(
                                context, "Algum campo esta vazio", Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(text = "Enviar")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Icon(imageVector = Icons.Outlined.Send, contentDescription = "SEND")
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
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
/*
fun validEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    val matcher = pattern.matcher(email)
    return matcher.matches()
}*/

@Composable
fun ButtonCustom(text: String, icon: ImageVector,clickable: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .clip(
                MaterialTheme.shapes.small.copy(
                    CornerSize(8.dp)
                )
            )
            .background(Color.White)
            .border(
                BorderStroke(1.dp, Color.DarkGray),
                MaterialTheme.shapes.small.copy(
                    CornerSize(8.dp)
                )
            )
            .padding(8.dp)
            .clickable {
                clickable.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Icon(
            imageVector = icon,
            contentDescription = "PostAdd",
        )
    }
}

