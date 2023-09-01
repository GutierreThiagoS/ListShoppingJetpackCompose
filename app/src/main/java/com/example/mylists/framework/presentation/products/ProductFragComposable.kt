package com.example.mylists.framework.presentation.products

import android.util.Log
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mylists.R
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.ui.theme.DarkGreen
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun ProductListItem(product: ProductOnItemShopping) {

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
        ) {
            ItemProduct(product = product)
        }
    }
}

@Composable
fun FloatingActionButtonItem(icon: ImageVector, size: Dp = 40.dp, onClick: (isLong: Boolean) -> Unit) {
    var isLongClickActive by remember { mutableStateOf(false) }

    val shape = if (icon == Icons.Filled.Add) {
        MaterialTheme.shapes.small.copy(
            topEnd = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        )
    } else MaterialTheme.shapes.small.copy(
        topStart = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    FloatingActionButton(
        modifier = Modifier
            .height(size)
            .width(size)
            .pointerInput(Unit) {
                Modifier.detectLongClick { longClickOccurred ->
                    isLongClickActive = longClickOccurred
                }
            },
        shape = shape,
        onClick = {
            onClick(isLongClickActive)
        }
    ) {
        Icon(icon, "Localized description")
    }
}

@Composable
fun ProductsFrag(
    viewModel: ProductViewModel = koinViewModel()
) {
    val productListState by viewModel.productListState.collectAsState(listOf())
    AdapterProducts(products = productListState)
}

@Composable
fun AdapterProducts(products: List<ProductOnItemShopping>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = products) { product ->
            ProductListItem(product = product)
        }
    }
}

@Composable
fun ItemProduct(
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

        FloatingActionButtonItem(Icons.Filled.Add) {
            product.quantity++
            text = product.quantity.toString()
            viewModel.insertProductInShoppingList(product = product)
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

        FloatingActionButtonItem(ImageVector.vectorResource(id = R.drawable.baseline_remove)) {
            if (product.quantity > 0) {
                product.quantity--
                text = product.quantity.toString()
                viewModel.insertProductInShoppingList(product = product)
            } /*else if (it) {
                Log.e("Teste", "ClickLong $it")
            }*/

            Log.e("Teste", "ClickLong $it")

        }
    }
}

fun Modifier.detectLongClick(callback: (Boolean) -> Unit): Modifier {
    return pointerInput(Unit) {
        var longClickStarted = false

        detectTransformGestures { _, _, pan, _ ->
            Log.e("pan", "pan ${pan.absoluteValue}")
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