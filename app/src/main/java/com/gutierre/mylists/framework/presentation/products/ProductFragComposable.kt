package com.gutierre.mylists.framework.presentation.products

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.framework.composable.CategoryInProductsLayout
import com.gutierre.mylists.framework.ui.main.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductsFrag(
    mainViewModel: MainViewModel,
    viewModel: ProductViewModel = koinViewModel(),
    editProductClick: (product: ProductOnItemShopping) -> Unit
) {
    val productListState by viewModel.productListState.collectAsState(listOf())
    
    if (productListState.isNotEmpty()) {
        CategoryInProductsLayout(
            mainViewModel = mainViewModel,
            products = productListState,
            editProductClick = editProductClick
        )
    } else Text(text = "Não há Produto!")
}
