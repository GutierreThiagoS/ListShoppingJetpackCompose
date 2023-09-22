package com.example.mylists.framework.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylists.NavigationScreen
import com.example.mylists.R
import com.example.mylists.framework.ui.main.MainViewModel
import com.example.mylists.framework.ui.theme.MyListsTheme
import com.example.mylists.framework.ui.theme.TealBlack10
import org.koin.androidx.compose.koinViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    MyListsTheme {
        ToolbarAppBar {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarAppBar(
    barcode: String? = null,
    viewModel: MainViewModel = koinViewModel(),
    readBarCode: () -> Unit
) {

    val navigationState by viewModel.navigationState.collectAsState()
    val total by viewModel.total.collectAsState(0f)

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
                            IconButton(
                                modifier = Modifier.size(50.dp),
                                onClick = {
                                    readBarCode()
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
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            BottomMenu(
                barcode = barcode,
                contentPadding = innerPadding,
                readBarCode = readBarCode
            )
        }
    )
}
