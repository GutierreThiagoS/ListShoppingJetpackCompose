package com.example.mylists.composable

import android.util.Log
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mylists.R
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.presentation.products.ProductViewModel
import com.example.mylists.framework.ui.theme.DarkGreen
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun ActionButtonAmountTextField(
    product: ProductOnItemShopping,
    isVisibleCheck : Boolean = false,
    viewModel: ProductViewModel = koinViewModel()
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isVisibleCheck) {
            val checkedState = remember { mutableStateOf(product.selected) }
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    product.selected = it
                    viewModel.insertProductInShoppingList(product = product)
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
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
        var text by rememberSaveable { mutableStateOf("${product.quantity}") }

        FloatingActionButtonItem(ImageVector.vectorResource(id = R.drawable.baseline_remove)) {
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

fun Modifier.detectLongClick(callback: (Boolean) -> Unit): Modifier {
    return pointerInput(Unit) {
        var longClickStarted = false

        detectTransformGestures { _, _, pan, _ ->
            Log.e("pan", "pan ${pan.absoluteValue.dp.toPx()}")
            if (!longClickStarted) {
                if (pan.absoluteValue >= 20.dp.toPx()) {
                    longClickStarted = true
                    callback(true)
                }
            }
        }

        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                Log.e("awaitPointerEventScope", "event.changes.all ${event.changes.all { it.positionChange().x.absoluteValue >= 20.dp.toPx() }}")

                if (event.changes.all { it.positionChange().x.absoluteValue >= 20.dp.toPx() }) {
                    longClickStarted = false
                    callback(false)
                    break
                } else {
                    longClickStarted = false
                    callback(false)
                    break
                }
            }
        }
    }
}