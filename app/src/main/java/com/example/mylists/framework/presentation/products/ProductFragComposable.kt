package com.example.mylists.framework.presentation.products

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mylists.composable.CategoryInProductsLayout
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductsFrag(
    viewModel: ProductViewModel = koinViewModel()
) {
    val productListState by viewModel.productListState.collectAsState(listOf())
    
    if (productListState.isNotEmpty()) {
        CategoryInProductsLayout(products = productListState)
    } else Text(text = "Não há Produto!")
}
