package com.gutierre.mylists.framework.composable

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.framework.presentation.products.ProductViewModel
import com.gutierre.mylists.framework.ui.theme.DarkGreen
import com.gutierre.mylists.framework.ui.theme.Teal700
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActionButtonAmountTextField(
    product: ProductOnItemShopping,
    isShopping : Boolean = false,
    viewModel: ProductViewModel = koinViewModel()
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = product.description)
            Text(
                text = "Ean: " + product.ean,
                style = MaterialTheme.typography.bodySmall
            )
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

        var text by rememberSaveable { mutableStateOf("${product.quantity}") }

        FloatingActionButtonItem(if (isShopping && product.quantity <= 0) Icons.Outlined.Delete else Icons.Outlined.Remove) {
            if (product.quantity > -1
                ) {
                product.quantity--
                text = product.quantity.toString()
                viewModel.insertProductInShoppingList(product = product)
            }
        }
        text = product.quantity.toString()
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
            containerColor = Teal700,
            onClick = {
                onClick(false)
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Localized description",
                tint = Color.White
            )
        }
}