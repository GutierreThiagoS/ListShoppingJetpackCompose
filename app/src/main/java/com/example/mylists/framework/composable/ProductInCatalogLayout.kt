package com.example.mylists.framework.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylists.domain.model.Category
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.presentation.products.ProductViewModel
import com.example.mylists.framework.ui.theme.Bordor
import com.example.mylists.framework.ui.theme.DarkGreen
import com.example.mylists.framework.ui.theme.Gray
import com.example.mylists.framework.ui.theme.GrayLight
import com.example.mylists.framework.ui.theme.Teal700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryInProductsLayout(products: List<ProductOnItemShopping>, isVisibleCheck : Boolean = false) {
    val categories = products.map { Category(it.idCategory, it.nameCategory) }
        .sortedBy { it.nameCategory }
        .distinct()

    LazyColumn {
        itemsIndexed(categories) { index, category ->
            val isProductDrop = remember { mutableStateOf(true) }

            OutlinedCard(
                Modifier
                    .fillMaxWidth().let {
                        if (index == 0) {
                            it.padding(4.dp)
                        } else it.padding(bottom = 4.dp).padding(horizontal = 4.dp)
                    }

            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GrayLight)
                            .clickable { isProductDrop.value = isProductDrop.value.not() }
                            .padding(start = 16.dp, top = 6.dp, bottom = 6.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,

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

                    var offsetX by remember { mutableFloatStateOf(0f) }

                    productsInCategory.forEachIndexed { index, product ->
                        if (isProductDrop.value) {
                            ShoppingProductListItem(
                                product = product,
                                offsetXGlobal = offsetX,
                                setOffsetXGlobal = { o, i -> offsetX = o },
                                index = index,
                                isVisibleCheck = isVisibleCheck)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingProductListItem(
    product: ProductOnItemShopping,
    index: Int,
    offsetXGlobal: Float,
    setOffsetXGlobal: (value: Float, index: Int) -> Unit,
    isVisibleCheck: Boolean = false,
    viewModel: ProductViewModel = koinViewModel()
) {

    var offsetX by remember { mutableFloatStateOf(offsetXGlobal) }
    val coroutineScope = rememberCoroutineScope()

    var editProduct by remember { mutableStateOf(false) }
    var newDescription by remember { mutableStateOf(product.description) }
    var newPrice by remember { mutableFloatStateOf(product.price) }

    if (index != 0 ) LineDivided()
    Box(
        Modifier.clickable {
            setOffsetXGlobal(0f, index)
        }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(
                if (offsetX < 0) Bordor else if (offsetX > 0) {
                    if (editProduct) DarkGreen
                    else Teal700
                } else Color.White
            )
            .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                if (editProduct) {
                    if (product.description != newDescription || product.price != newPrice) {
                        viewModel.editProduct(product, newDescription, newPrice) {
                            editProduct = false
                            offsetX = 0f
                        }
                    } else editProduct = false
                } else editProduct = true
            }) {
                Icon(imageVector = if (editProduct) Icons.Outlined.Save else Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.White)
            }
            IconButton(onClick = { viewModel.removeProduct(product) { offsetX = 0f } }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Deletar", tint = Color.White)
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    translationX = offsetX
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        offsetX += dragAmount
                        val offset = offsetX
                        coroutineScope.launch {
                            delay(5000)
                            if (offset == offsetX && !editProduct) {
                                offsetX = 0f
                            }
                        }
                        when {
                            offsetX < -200 -> offsetX = -200f
                            offsetX > 200 -> offsetX = 200f
                            else -> {
                                val offsetXLocal = offsetX
                                coroutineScope.launch {
                                    delay(500)
                                    if (offsetX < 200 && offsetX > -200 && offsetXLocal == offsetX) {
                                        offsetX = 0f
                                        editProduct = false
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            Column(modifier = Modifier.let {
                if (isVisibleCheck) it.padding(vertical = 16.dp).padding(end = 16.dp)
                else it.padding(16.dp)
            }
                .fillMaxWidth()
            ) {
                ActionButtonAmountTextField(
                    product = product,
                    isVisibleCheck = isVisibleCheck,
                    isEdit = editProduct,
                    newDescription = newDescription,
                    newPrice = newPrice,
                    newDescriptionChange = { newDescription = it},
                    newPriceChange = { newPrice = it.toFloat() }
                )
            }
        }
    }
}

@Composable
fun LineDivided() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .padding(horizontal = 8.dp)
            .background(Gray)
    )
}