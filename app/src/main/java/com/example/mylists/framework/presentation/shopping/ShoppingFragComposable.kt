package com.example.mylists.framework.presentation.shopping

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylists.R
import com.example.mylists.framework.composable.CategoryInProductsLayout
import com.example.mylists.framework.ui.theme.MyListsTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShoppingFrag(viewModel: ShoppingViewModel = koinViewModel()) {

    val productOnShoppingState by viewModel.productOnShoppingState.collectAsState(initial = listOf())

    if (productOnShoppingState.isNotEmpty()) {
        CategoryInProductsLayout(products = productOnShoppingState, isVisibleCheck = true)
    } else {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(500.dp),
                    painter = painterResource(id = R.mipmap.carrinho_vazio),
                    contentDescription = "cart not")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShoppingPreview() {
    MyListsTheme {
        ShoppingFrag()
    }
}