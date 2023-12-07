package com.gutierre.mylists.framework.presentation.add

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
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.NavigationScreen
import com.gutierre.mylists.R
import com.gutierre.mylists.domain.model.Product
import com.gutierre.mylists.framework.composable.AutocompleteOutlinedTextField
import com.gutierre.mylists.framework.composable.FloatingActionButtonItem
import com.gutierre.mylists.framework.presentation.dialog.DialogArgs
import com.gutierre.mylists.framework.presentation.dialog.ShowBaseDialog
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.framework.utils.toFloatNotNull
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProductFrag(
    viewModel: AddNewProductViewModel = koinViewModel(),
    mainViewModel: MainViewModel,
    productBarCode: Product? = null,
    categoryState: String? = null,
    returnDest: (destination: String) -> Unit
) {

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

    logE("productState $productBarCode")
    logE("categoryList $productBarCode ${categoryList.find { it.idCategory == productBarCode?.idCategoryFK }?.nameCategory}")

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

            var textDescription by remember { mutableStateOf(productBarCode?.description ?: "") }
            OutlinedEditText(
                label = "Descrição",
                textValue = textDescription,
            ) { text ->
                textDescription = text
            }

            var textBrand by remember { mutableStateOf(productBarCode?.brandProduct ?: "") }
            OutlinedEditText(
                label = "Marca",
                textValue = textBrand,
                suggestions = brandList
            ) { text ->
                textBrand = text
            }

            var textCategory by remember { mutableStateOf(categoryState ?: "") }
            OutlinedEditText(
                label = "Categoria",
                textValue = textCategory,
                suggestions = categoryList.map { it.nameCategory }
            ) { text ->
                textCategory = text
            }

            var textBarCode by remember { mutableStateOf(productBarCode?.ean ?: "") }
            OutlinedEditText(
                label = "Código de Barra",
                textValue = textBarCode,
                keyboardType = KeyboardType.Number
            ) { text ->
                textBarCode = text
            }

            var textPrice by remember { mutableStateOf("${productBarCode?.price ?: ""}") }
            OutlinedEditText(
                label = "Preço",
                textValue = textPrice,
                keyboardType = KeyboardType.Number
            ) { text ->
                textPrice = text
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

            Row(modifier = Modifier.align(Alignment.End)) {
                FilledTonalButton(
                    onClick = {
                        textDescription = ""
                        textCategory = ""
                        textBarCode = ""
                        textBrand = ""
                        textPrice = ""
                        returnDest("")
                    }
                ) {
                    Text(text = "Limpar")
                }

                FilledTonalButton(
                    onClick = {
                        if (
                            textDescription.isNotBlank() &&
//                            textBrand.isNotBlank() &&
//                            textBarCode.isNotBlank() &&
                            textCategory.isNotBlank() &&
                            textPrice.isNotBlank()
                        ) {
                            isLoading = true
                            viewModel.consultCategory(textCategory) { category ->
                                if (category != null) {
                                    val product = Product(
                                        idProduct = productBarCode?.idProduct ?: 0,
                                        description = textDescription,
                                        brandProduct = textBrand.ifBlank { "Não Definido" },
                                        idCategoryFK = category.idCategory,
                                        ean = textBarCode,
                                        price = textPrice.toFloatNotNull(),
                                        categoryName = category.nameCategory
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
                                            textDescription = ""
                                            textBrand = ""
                                            textBarCode = ""
                                            textCategory = ""
                                            textPrice = ""
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedEditText(
    modifier: Modifier = Modifier,
    label: String,
    textValue: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    suggestions: List<String> = emptyList(),
    returnText: (text: String) -> Unit
) {
    var text by remember { mutableStateOf(textValue) }
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
            label =  {
                Text(label)
            },
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
            modifier = modifier.fillMaxWidth(),
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
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