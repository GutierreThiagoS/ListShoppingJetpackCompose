package com.example.mylists.framework.presentation.shopping

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import com.example.mylists.domain.model.ProductOnItemShopping
import com.example.mylists.framework.presentation.products.ItemProduct
import com.example.mylists.framework.ui.theme.MyListsTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun ShoppingListItem(
    product: ProductOnItemShopping,
) {

    /*val expanded = remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )*/

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
        ) {
            ItemProduct(product, true)
        }
    }
}

@Composable
fun ShoppingFrag(viewModel: ShoppingViewModel = koinViewModel()) {

    val productOnShoppingState by viewModel.productOnShoppingState.collectAsState(initial = listOf())

    if (productOnShoppingState.isNotEmpty()) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = productOnShoppingState) { product ->
                ShoppingListItem(product = product)
            }
        }
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