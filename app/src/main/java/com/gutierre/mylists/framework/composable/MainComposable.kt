package com.gutierre.mylists.framework.composable

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gutierre.mylists.NavigationScreen
import com.gutierre.mylists.R
import com.gutierre.mylists.framework.ui.firebase.FireBaseProductActivity
import com.gutierre.mylists.framework.ui.main.MainViewModel
import com.gutierre.mylists.framework.ui.theme.MyListsTheme
import com.gutierre.mylists.framework.ui.theme.TealBlack10
import org.koin.androidx.compose.koinViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    MyListsTheme {
        val viewModel: MainViewModel = koinViewModel()
        ToolbarAppBar(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarAppBar(
    viewModel: MainViewModel
) {

    val navigationState by viewModel.navigationState.collectAsState()
    val total by viewModel.total.collectAsState(0f)

    val context = LocalContext.current


    val textSearch by viewModel.textSearch.collectAsState()

    val isSearch by viewModel.isSearch.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = navigationState.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = TealBlack10,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    )
                },
                actions = {
                    if (navigationState.title == NavigationScreen.SHOPPING.label ||
                        navigationState.title == NavigationScreen.PRODUCTS.label) {
                        Row {
                            if (!isSearch) {
                                if (navigationState.title == NavigationScreen.SHOPPING.label) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .align(Alignment.CenterVertically),
                                        imageVector = Icons.Filled.Calculate,
                                        contentDescription = "Localized description"
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(end = 5.dp)
                                            .align(Alignment.CenterVertically),
                                        text = String.format("R$ %.2f", total ?: 0f),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                if (navigationState.title == NavigationScreen.PRODUCTS.label) {
                                    IconButton(
                                        modifier = Modifier.size(50.dp),
                                        onClick = {
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    FireBaseProductActivity::class.java
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(horizontal = 10.dp)
                                                .align(Alignment.CenterVertically),
                                            imageVector = Icons.Outlined.CloudDownload,
//                                        painter = painterResource(R.drawable.icon_database_vector),
                                            contentDescription = "Buscar no Banco"
                                        )
                                    }
                                }
                                IconButton(
                                    modifier = Modifier.size(50.dp),
                                    onClick = {
                                        viewModel.activeBarCode()
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(horizontal = 10.dp)
                                            .align(Alignment.CenterVertically),
                                        painter = painterResource(R.drawable.barcode_scanner),
                                        contentDescription = "Localized description"
                                    )
                                }
                            } else {
                                OutlinedTextField(value = textSearch ?: "", onValueChange = {
                                    viewModel.setTextSearch(it)
                                })
                            }

                            IconButton(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    viewModel.setTextSearch()
                                    viewModel.setStatusSearch(!isSearch)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(horizontal = 10.dp)
                                        .align(Alignment.CenterVertically),
                                    imageVector = if (isSearch) Icons.Outlined.SearchOff else Icons.Outlined.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    }
                }
            )
        },
        content = { innerPadding ->

            BottomMenu(
                contentPadding = innerPadding,
                mainViewModel = viewModel
            )
        }
    )
}
