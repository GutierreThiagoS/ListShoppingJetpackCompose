package com.example.mylists.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.ui.theme.Gray
import com.example.mylists.framework.ui.theme.GrayLight


@Composable
fun CategoryInProductsLayout(products: List<ProductOnItemShopping>, isVisibleCheck : Boolean = false) {
    val categories = products.map { Category(it.idCategory, it.nameCategory) }
        .sortedBy { it.nameCategory }
        .distinct()

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
        items(categories) { category ->
            val isProductDrop = remember { mutableStateOf(true) }

            OutlinedCard(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GrayLight)
                            .padding(start = 16.dp, top = 6.dp, bottom = 6.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.nameCategory,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        IconButton(onClick = { isProductDrop.value = isProductDrop.value.not() }) {
                            Icon(imageVector = if (isProductDrop.value) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown, contentDescription = "Icone Grupo")
                        }
                    }


                    val productsInCategory =
                        products.filter { it.nameCategory == category.nameCategory }
                    productsInCategory.forEachIndexed { i, product ->
                        if (isProductDrop.value) {
                            ShoppingProductListItem(product = product, index = i, isVisibleCheck = isVisibleCheck)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingProductListItem(product: ProductOnItemShopping, index: Int, isVisibleCheck : Boolean = false) {

    if (index != 0 ) Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .padding(horizontal = 8.dp)
            .background(Gray)
    )
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            ActionButtonAmountTextField(product = product, isVisibleCheck = isVisibleCheck)
        }
    }
}

@Composable
fun FloatingActionButtonItem(icon: ImageVector, size: Dp = 40.dp, onClick: (isLong: Boolean) -> Unit) {
    var isLongClickActive by remember { mutableStateOf(false) }

    val shape = if (icon == Icons.Filled.Add) {
        MaterialTheme.shapes.small.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        )
    } else MaterialTheme.shapes.small.copy(
        topEnd = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
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