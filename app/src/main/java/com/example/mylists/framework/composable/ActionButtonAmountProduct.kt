package com.example.mylists.framework.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.presentation.products.ProductViewModel
import com.example.mylists.framework.ui.theme.DarkGreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionButtonAmountTextField(
    product: ProductOnItemShopping,
    isVisibleCheck : Boolean = false,
    isEdit: Boolean = false,
    newDescription: String,
    newPrice: Float,
    newDescriptionChange: (value: String) -> Unit,
    newPriceChange: (value: String) -> Unit,
    viewModel: ProductViewModel = koinViewModel()
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isVisibleCheck && !isEdit) {
            var checkedState by remember { mutableStateOf(product.selected) }
            Checkbox(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    product.selected = it
                    viewModel.insertProductInShoppingList(product = product)
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            if (isEdit) {
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = {
                        newDescriptionChange(it)
                    },
                    label = { Text(text = "Descrição") }
                )

                OutlinedTextField(
                    value = newPrice.toString(),
                    onValueChange = {
                        newPriceChange(it)
                    },
                    label = { Text(text = "Preço") }
                )
            } else {
                Text(text = product.description)
                Text(
                    text = if (product.quantity > 1) {
                        String.format(
                            "R$ %.2f (%.2f * %d)",
                            product.price * product.quantity,
                            product.price,
                            product.quantity
                        )
                    } else String.format("R$ %.2f", product.price),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = DarkGreen
                )
            }
        }
        var text by rememberSaveable { mutableStateOf("${product.quantity}") }

        FloatingActionButtonItem(Icons.Outlined.Remove) {
            if (product.quantity > 0) {
                product.quantity--
                text = product.quantity.toString()
                viewModel.insertProductInShoppingList(product = product)
            }
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
                    product.quantity = quantity
                    viewModel.insertProductInShoppingList(product = product)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        FloatingActionButtonItem(Icons.Filled.Add) {
            product.quantity++
            text = product.quantity.toString()
            viewModel.insertProductInShoppingList(product = product)
        }
    }
}

@Composable
fun FloatingActionButtonItem(icon: ImageVector, size: Dp = 40.dp, onClick: (isLong: Boolean) -> Unit) {
    val shape = if (icon == Icons.Filled.Add) {
            MaterialTheme.shapes.small.copy(
                topStart = CornerSize(0.dp),
                bottomStart = CornerSize(0.dp)
            )
        } else {
            MaterialTheme.shapes.small.copy(
                topEnd = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .height(size)
                .width(size),
            shape = shape,
            onClick = {
                onClick(false)
            }
        ) {
            Icon(icon, "Localized description")
        }
}