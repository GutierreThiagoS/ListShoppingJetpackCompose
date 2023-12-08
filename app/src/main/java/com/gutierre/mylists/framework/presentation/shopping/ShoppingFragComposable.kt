package com.gutierre.mylists.framework.presentation.shopping

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.R
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.framework.composable.CategoryInProductsLayout
import com.gutierre.mylists.framework.ui.theme.VeryLightGray
import com.gutierre.mylists.framework.ui.theme.MyListsTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShoppingFrag(
    viewModel: ShoppingViewModel = koinViewModel(),
    editProductClick: (product: ProductOnItemShopping) -> Unit
) {

    val productOnShoppingState by viewModel.productOnShoppingState.collectAsState(initial = listOf())

    if (productOnShoppingState.isNotEmpty()) {
        CategoryInProductsLayout(
            products = productOnShoppingState,
            isVisibleCheck = true,
            editProductClick = editProductClick
        )
    } else {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(VeryLightGray),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(500.dp),
                        painter = painterResource(id = R.mipmap.carrinho_vazio),
                        contentDescription = "cart not")
                    Text(text = "Escolha seus Produtos -> ")
                }
                
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShoppingPreview() {
    MyListsTheme {
        ShoppingFrag {}
    }
}