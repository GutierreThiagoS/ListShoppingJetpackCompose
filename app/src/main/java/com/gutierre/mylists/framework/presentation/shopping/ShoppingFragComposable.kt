package com.gutierre.mylists.framework.presentation.shopping

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.R
import com.gutierre.mylists.domain.model.ProductOnItemShopping
import com.gutierre.mylists.framework.composable.CategoryInProductsLayout
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.ui.theme.VeryLightGray
import com.gutierre.mylists.framework.ui.theme.MyListsTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShoppingFrag(
    mainViewModel: MainViewModel,
    viewModel: ShoppingViewModel = koinViewModel(),
    editProductClick: (product: ProductOnItemShopping) -> Unit
) {

    val productOnShoppingState by viewModel.productOnShoppingState.collectAsState(initial = listOf())

    if (productOnShoppingState.isNotEmpty()) {
        CategoryInProductsLayout(
            mainViewModel = mainViewModel,
            products = productOnShoppingState,
            isShopping = true,
            editProductClick = editProductClick
        )
    } else {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VeryLightGray),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        painter = painterResource(id = R.mipmap.carrinho_vazio),
                        contentDescription = "cart not")
                    Text(
                        text = "Escolha seus Produtos",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        modifier = Modifier.padding(end = 100.dp, top = 10.dp),
                        imageVector = Icons.Outlined.ArrowDownward,
                        contentDescription = "ArrowDownward"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShoppingPreview() {
    MyListsTheme {
//        ShoppingFrag() {}
    }
}