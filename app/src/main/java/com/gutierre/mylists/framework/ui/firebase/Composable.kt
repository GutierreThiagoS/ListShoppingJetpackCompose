package com.gutierre.mylists.framework.ui.firebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.framework.ui.theme.DarkGreen
import com.gutierre.mylists.framework.ui.theme.MyListsTheme
import com.gutierre.mylists.framework.ui.theme.TealBlack10
import com.gutierre.mylists.framework.ui.theme.VeryLightGray
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsFireBase(
    activity: FireBaseProductActivity? = null,
    viewModel: FireBaseViewModel = koinViewModel()
) {

    val productList = viewModel.flowFireBaseStore.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.getFireBaseStore()
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            modifier = Modifier.size(50.dp),
                            onClick = {
                                activity?.finish()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(horizontal = 10.dp)
                                    .align(Alignment.CenterVertically),
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                        Text(
                            text = "Produtos Compartilhados",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = TealBlack10,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            color = VeryLightGray
        ) {
            LazyColumn(content = {
                items(productList.value) { product ->
                    Card(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                    .padding(8.dp)
                                ) {
                                    Text(text = product.description)
                                    Text(
                                        text = "Ean: " + product.ean,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = String.format("R$ %.2f", product.price),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = DarkGreen
                                    )
                                }

                                IconButton(onClick = {
                                    viewModel.saveProductFireBase(product, context)
                                }) {
                                    Icon(imageVector = Icons.Outlined.SaveAlt, contentDescription = "SaveAlt")
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsFireBasePreview() {
    MyListsTheme {
        ProductsFireBase()
    }
}