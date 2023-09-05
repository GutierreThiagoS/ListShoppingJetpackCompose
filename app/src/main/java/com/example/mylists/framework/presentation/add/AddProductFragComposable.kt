package com.example.mylists.framework.presentation.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylists.NavigationScreen
import com.example.mylists.R
import com.example.mylists.composable.AutocompleteOutlinedTextField
import com.example.mylists.composable.FloatingActionButtonItem
import com.example.mylists.domain.model.Product
import com.example.mylists.framework.presentation.dialog.DialogArgs
import com.example.mylists.framework.presentation.dialog.ShowBaseDialog
import com.example.mylists.framework.ui.main.MainViewModel
import com.example.mylists.framework.utils.notNull
import com.example.mylists.framework.utils.toFloatNotNull
import org.koin.androidx.compose.koinViewModel

val itemInputs = mutableListOf<ItemFieldAddProd>()

@Composable
fun AddProductFrag(viewModel: AddNewProductViewModel = koinViewModel(), mainViewModel: MainViewModel = koinViewModel(), returnDest: (destination: String) -> Unit) {

    var showDialog by remember { mutableStateOf(
        DialogArgs("Error", "Existe Campo vazio!")
    ) }
    var isLoading by remember { mutableStateOf(false) }

    if (showDialog.isShow) {
        ShowBaseDialog(showDialog) {
            showDialog = DialogArgs(isShow = false)
        }
    }

    if (isLoading) {
        ProgressSpinner()
    }

    var quantityShopping by rememberSaveable { mutableStateOf("1") }

    val (checkedState, onStateChange) = remember { mutableStateOf(true) }

    val categoryList by viewModel.categoryList.collectAsState(listOf())

    val brandList by viewModel.brandList.collectAsState(listOf())

    if (itemInputs.isEmpty()) {
        itemInputs.addAll(
            mutableListOf(
                ItemFieldAddProd(
                    label = "Descrição"
                ),
                ItemFieldAddProd(
                    label = "Marca"
                ),
                ItemFieldAddProd(
                    label = "Categoria"
                ),
                ItemFieldAddProd(
                    label = "Preço",
                    keyboardType = KeyboardType.Number
                )
            )
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                text = "Adicionar novo Produto!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            itemInputs.forEach { itemField ->
                OutlinedEditText(
                    label = itemField.label,
                    keyboardType = itemField.keyboardType,
                    suggestions = when (itemField.label) {
                        "Categoria" -> categoryList.map { it.nameCategory }
                        "Marca" -> brandList
                        else -> listOf()
                    }
                ) { text ->
                    itemField.textEdit = text
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .toggleable(
                        value = checkedState,
                        onValueChange = { onStateChange(!checkedState) },
                        role = Role.Checkbox
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = "Adicionar no Carrinho",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )

                if (checkedState) {
                    InsertQuantityShopping {
                        quantityShopping = it
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = {
                    if (itemInputs.none { it.textEdit.isBlank() }) {
                        isLoading = true
                        viewModel.consultCategory(
                            findItem("Categoria").notNull()
                        ) { category ->
                            if (category != null) {
                                val product = Product(
                                    description = findItem("Descrição").notNull(),
                                    brandProduct = findItem("Marca").notNull(),
                                    idCategoryFK = category.idCategory,
                                    price = findItem("Preço").toFloatNotNull()
                                )
                                viewModel.insertProduct(product = product, if (checkedState) quantityShopping else "") {
                                    if (it) {
                                        if (checkedState) {
                                            mainViewModel.setNavigation(title = NavigationScreen.SHOPPING.label)
                                            returnDest(NavigationScreen.SHOPPING.label)
                                        }
                                        else {
                                            mainViewModel.setNavigation(title = NavigationScreen.PRODUCTS.label)
                                            returnDest(NavigationScreen.PRODUCTS.label)
                                        }
                                    } else {
                                        showDialog = DialogArgs("Error", "Produto não salvo!",true)
                                    }
                                }
                            }
                            isLoading = false
                        }
                    } else showDialog = DialogArgs("Error", "Existe Campo vazio!",true)
                },
            ) {
                Text(text = "Adicionar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun OutlinedEditText(
    label: String,
    keyboardType: KeyboardType,
    suggestions: List<String> = emptyList(),
    returnText: (text: String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    if (suggestions.isNotEmpty())
        AutocompleteOutlinedTextField(
            value = text,
            suggestions = suggestions,
            onValueChange = { newText ->
                isError = text.isNotBlank() && newText.isBlank()
                text = newText
                returnText(newText)
            },
            label =  { Text(label) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            isError = isError,
        )
    else {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                isError = text.isNotBlank() && newText.isBlank()
                text = newText
                returnText(newText)
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

fun findItem(value: String): String? {
    return itemInputs.find { it.label == value }?.textEdit
}

@Composable
fun InsertQuantityShopping(returnText: (text: String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("1") }

    Row(modifier = Modifier.padding(start = 5.dp)) {

        FloatingActionButtonItem(ImageVector.vectorResource(id = R.drawable.baseline_remove), 30.dp) {
            val quantity = (text.toIntOrNull() ?: 0) - 1
            text = "${ if (quantity > 1) quantity else 1}"
            returnText(text)
        }

        BasicTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp),
            value = text,
            onValueChange = {
                text = it
                val quantity = it.toIntOrNull()
                if (quantity != null && quantity > 0) {
                    returnText("$quantity")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        FloatingActionButtonItem(Icons.Filled.Add, 30.dp) {
            val quantity = text.toIntOrNull() ?: 0
            text = "${quantity + 1}"
            returnText(text)
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AddProductFragPreview() {
    AddProductFrag {}
}